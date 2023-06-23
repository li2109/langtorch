package ai.knowly.langtorch.llm.openai;

import ai.knowly.langtorch.hub.module.cache.EnableLLMCache;
import ai.knowly.langtorch.hub.module.token.EnableOpenAITokenRecord;
import ai.knowly.langtorch.llm.openai.schema.config.OpenAIProxyConfig.ProxyType;
import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import ai.knowly.langtorch.llm.openai.schema.dto.OpenAIError;
import ai.knowly.langtorch.llm.openai.schema.dto.OpenAIHttpParseException;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditResult;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageEditRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageVariationRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.ImageResult;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationResult;
import ai.knowly.langtorch.llm.openai.schema.exception.OpenAIApiExecutionException;
import ai.knowly.langtorch.llm.openai.schema.exception.OpenAIServiceInterruptedException;
import ai.knowly.langtorch.utils.future.retry.FutureRetrier;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/** The OpenAIService provides methods for calling the OpenAI API and handling errors. */
public class OpenAIService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.openai.com/";
  private static final ObjectMapper mapper = defaultObjectMapper();
  private static final String RESPONSE_FORMAT = "response_format";
  private static final MediaType MULTI_PART_FORM_DATA = MediaType.parse("multipart/form-data");
  private static final String IMAGE = "image";
  private static final MediaType IMAGE_MEDIA_TYPE = MediaType.parse(IMAGE);

  private final OpenAIApi api;
  private final FutureRetrier futureRetrier;

  private final ScheduledExecutorService scheduledExecutor;

  @Inject
  public OpenAIService(final OpenAIServiceConfig openAIServiceConfig) {
    ObjectMapper defaultObjectMapper = defaultObjectMapper();
    OkHttpClient client = buildClient(openAIServiceConfig);
    Retrofit retrofit = defaultRetrofit(client, defaultObjectMapper);
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    this.futureRetrier =
        new FutureRetrier(
            scheduledExecutor,
            openAIServiceConfig.backoffStrategy(),
            openAIServiceConfig.retryConfig());
    this.api = retrofit.create(OpenAIApi.class);
  }

  /** Calls the Open AI api, returns the response, and parses error messages if the request fails */
  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      // Restore the interrupt status
      Thread.currentThread().interrupt();
      // Optionally, log or handle the exception here.
      logger.atSevere().withCause(e).log("Thread was interrupted during API call.");
      throw new OpenAIServiceInterruptedException(e);

    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
          OpenAIError error = mapper.readValue(errorBody, OpenAIError.class);
          throw new OpenAIHttpParseException(error, e, httpException.code());
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }
      throw new OpenAIApiExecutionException(e);
    }
  }

  public static ObjectMapper defaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return mapper;
  }

  public static OkHttpClient buildClient(OpenAIServiceConfig openAIServiceConfig) {
    Builder builder =
        new Builder()
            .addInterceptor(new OpenAIAuthenticationInterceptor(openAIServiceConfig.apiKey()))
            .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
            .readTimeout(openAIServiceConfig.timeoutDuration().toMillis(), TimeUnit.MILLISECONDS);

    openAIServiceConfig
        .proxyConfig()
        .ifPresent(
            proxyConfig ->
                builder.proxy(
                    new Proxy(
                        convertProxyEnum(proxyConfig.proxyType()),
                        new InetSocketAddress(proxyConfig.proxyHost(), proxyConfig.proxyPort()))));
    return builder.build();
  }

  public static Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
        .build();
  }

  private static Type convertProxyEnum(ProxyType proxyType) {
    if (proxyType == ProxyType.HTTP) {
      return Type.HTTP;
    } else if (proxyType == ProxyType.SOCKS) {
      return Type.SOCKS;
    } else {
      throw new IllegalArgumentException("Unknown proxy type: " + proxyType);
    }
  }

  @NotNull
  private static MultipartBody.Builder getMultipartBodyDefaultBuilder(
      CreateImageEditRequest request, RequestBody imageBody) {
    return new MultipartBody.Builder()
        .setType(MULTI_PART_FORM_DATA)
        .addFormDataPart("prompt", request.getPrompt())
        .addFormDataPart("size", request.getSize())
        .addFormDataPart(RESPONSE_FORMAT, request.getResponseFormat())
        .addFormDataPart(IMAGE, IMAGE, imageBody);
  }

  public CompletionResult createCompletion(CompletionRequest request) {
    return execute(createCompletionAsync(request));
  }

  @EnableLLMCache
  @EnableOpenAITokenRecord
  public ListenableFuture<CompletionResult> createCompletionAsync(CompletionRequest request) {
    return futureRetrier.runWithRetries(() -> {
      ListenableFuture<CompletionResult> completion = api.createCompletion(request);
      return completion;
    }, result -> true);
  }

  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    return execute(createChatCompletionAsync(request));
  }

  @EnableOpenAITokenRecord
  @EnableLLMCache
  public ListenableFuture<ChatCompletionResult> createChatCompletionAsync(
      ChatCompletionRequest request) {
    return futureRetrier.runWithRetries(() -> api.createChatCompletion(request), result -> true);
  }

  public EditResult createEdit(EditRequest request) {
    return execute(createEditAsync(request));
  }

  public ListenableFuture<EditResult> createEditAsync(EditRequest request) {
    return futureRetrier.runWithRetries(() -> api.createEdit(request), result -> true);
  }

  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    return execute(createEmbeddingsAsync(request));
  }

  public ListenableFuture<EmbeddingResult> createEmbeddingsAsync(EmbeddingRequest request) {
    return futureRetrier.runWithRetries(() -> api.createEmbeddings(request), result -> true);
  }

  public ImageResult createImage(CreateImageRequest request) {
    return execute(createImageAsync(request));
  }

  public ListenableFuture<ImageResult> createImageAsync(CreateImageRequest request) {
    return futureRetrier.runWithRetries(() -> api.createImage(request), result -> true);
  }

  public ImageResult createImageEdit(
      CreateImageEditRequest request, String imagePath, String maskPath) {
    File image = new File(imagePath);
    File mask = null;
    if (maskPath != null) {
      mask = new File(maskPath);
    }
    return createImageEdit(request, image, mask);
  }

  public ListenableFuture<ImageResult> createImageEditAsync(
      CreateImageEditRequest request, String imagePath, String maskPath) {
    File image = new File(imagePath);
    File mask = null;
    if (maskPath != null) {
      mask = new File(maskPath);
    }
    return createImageEditAsync(request, image, mask);
  }

  public ImageResult createImageEdit(CreateImageEditRequest request, File image, File mask) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder = getMultipartBodyDefaultBuilder(request, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(mask, IMAGE_MEDIA_TYPE);
      builder.addFormDataPart("mask", "mask", maskBody);
    }

    return execute(
        futureRetrier.runWithRetries(() -> api.createImageEdit(builder.build()), result -> true));
  }

  public ListenableFuture<ImageResult> createImageEditAsync(
      CreateImageEditRequest request, File image, File mask) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder = getMultipartBodyDefaultBuilder(request, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(mask, IMAGE_MEDIA_TYPE);
      builder.addFormDataPart("mask", "mask", maskBody);
    }
    return futureRetrier.runWithRetries(() -> api.createImageEdit(builder.build()), result -> true);
  }

  public ImageResult createImageVariation(CreateImageVariationRequest request, String imagePath) {
    File image = new File(imagePath);
    return createImageVariation(request, image);
  }

  public ListenableFuture<ImageResult> createImageVariationAsync(
      CreateImageVariationRequest request, String imagePath) {
    File image = new File(imagePath);
    return createImageVariationAsync(request, image);
  }

  public ImageResult createImageVariation(CreateImageVariationRequest request, File image) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MULTI_PART_FORM_DATA)
            .addFormDataPart("size", request.getSize())
            .addFormDataPart(RESPONSE_FORMAT, request.getResponseFormat())
            .addFormDataPart(IMAGE, IMAGE, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    return execute(
        futureRetrier.runWithRetries(
            () -> api.createImageVariation(builder.build()), result -> true));
  }

  public ListenableFuture<ImageResult> createImageVariationAsync(
      CreateImageVariationRequest request, File image) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MULTI_PART_FORM_DATA)
            .addFormDataPart("size", request.getSize())
            .addFormDataPart(RESPONSE_FORMAT, request.getResponseFormat())
            .addFormDataPart(IMAGE, IMAGE, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    return futureRetrier.runWithRetries(
        () -> api.createImageVariation(builder.build()), result -> true);
  }

  public ModerationResult createModeration(ModerationRequest request) {
    return execute(
        futureRetrier.runWithRetries(() -> api.createModeration(request), result -> true));
  }

  public ListenableFuture<ModerationResult> createModerationAsync(ModerationRequest request) {
    return futureRetrier.runWithRetries(() -> api.createModeration(request), result -> true);
  }
}
