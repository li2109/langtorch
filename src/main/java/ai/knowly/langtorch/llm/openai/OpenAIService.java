package ai.knowly.langtorch.llm.openai;

import ai.knowly.langtorch.llm.openai.schema.OpenAIApiExecutionException;
import ai.knowly.langtorch.llm.openai.schema.OpenAIServiceInterruptedException;
import ai.knowly.langtorch.llm.openai.schema.config.OpenAIProxyConfig;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import org.jetbrains.annotations.NotNull;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * OpenAIService wraps OpenAIApi and provides a synchronous and asynchronous interface to the OpenAI
 * API.
 */
public class OpenAIService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.openai.com/";
  private static final ObjectMapper mapper = defaultObjectMapper();
  private static final String RESPONSE_FORMAT = "response_format";
  private static final MediaType MULTI_PART_FORM_DATA = MediaType.parse("multipart/form-data");
  private static final String IMAGE = "image";
  private static final MediaType IMAGE_MEDIA_TYPE = MediaType.parse(IMAGE);

  private final OpenAIApi api;

  @Inject
  public OpenAIService(final OpenAIServiceConfig openAIServiceConfig) {
    ObjectMapper defaultObjectMapper = defaultObjectMapper();
    OkHttpClient client = buildClient(openAIServiceConfig);
    Retrofit retrofit = defaultRetrofit(client, defaultObjectMapper);
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

  public static OpenAIApi buildApi(String token, Duration timeout) {
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client =
        buildClient(
            OpenAIServiceConfig.builder().setApiKey(token).setTimeoutDuration(timeout).build());
    Retrofit retrofit = defaultRetrofit(client, mapper);
    return retrofit.create(OpenAIApi.class);
  }

  public static ObjectMapper defaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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

  private static Proxy.Type convertProxyEnum(ProxyType proxyType) {
    if (proxyType == OpenAIProxyConfig.ProxyType.HTTP) {
      return Proxy.Type.HTTP;
    } else if (proxyType == OpenAIProxyConfig.ProxyType.SOCKS) {
      return Proxy.Type.SOCKS;
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
    return execute(api.createCompletion(request));
  }

  public ListenableFuture<CompletionResult> createCompletionAsync(CompletionRequest request) {
    return api.createCompletion(request);
  }

  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    return execute(api.createChatCompletion(request));
  }

  public ListenableFuture<ChatCompletionResult> createChatCompletionAsync(
      ChatCompletionRequest request) {
    return api.createChatCompletion(request);
  }

  public EditResult createEdit(EditRequest request) {
    return execute(api.createEdit(request));
  }

  public ListenableFuture<EditResult> createEditAsync(EditRequest request) {
    return api.createEdit(request);
  }

  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    return execute(api.createEmbeddings(request));
  }

  public ListenableFuture<EmbeddingResult> createEmbeddingsAsync(EmbeddingRequest request) {
    return api.createEmbeddings(request);
  }

  public ImageResult createImage(CreateImageRequest request) {
    return execute(api.createImage(request));
  }

  public ListenableFuture<ImageResult> createImageAsync(CreateImageRequest request) {
    return api.createImage(request);
  }

  public ImageResult createImageEdit(
      CreateImageEditRequest request, String imagePath, String maskPath) {
    java.io.File image = new java.io.File(imagePath);
    java.io.File mask = null;
    if (maskPath != null) {
      mask = new java.io.File(maskPath);
    }
    return createImageEdit(request, image, mask);
  }

  public ListenableFuture<ImageResult> createImageEditAsync(
      CreateImageEditRequest request, String imagePath, String maskPath) {
    java.io.File image = new java.io.File(imagePath);
    java.io.File mask = null;
    if (maskPath != null) {
      mask = new java.io.File(maskPath);
    }
    return createImageEditAsync(request, image, mask);
  }

  public ImageResult createImageEdit(
      CreateImageEditRequest request, java.io.File image, java.io.File mask) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder = getMultipartBodyDefaultBuilder(request, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(mask, IMAGE_MEDIA_TYPE);
      builder.addFormDataPart("mask", "mask", maskBody);
    }

    return execute(api.createImageEdit(builder.build()));
  }

  public ListenableFuture<ImageResult> createImageEditAsync(
      CreateImageEditRequest request, java.io.File image, java.io.File mask) {
    RequestBody imageBody = RequestBody.create(image, IMAGE_MEDIA_TYPE);

    MultipartBody.Builder builder = getMultipartBodyDefaultBuilder(request, imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(mask, IMAGE_MEDIA_TYPE);
      builder.addFormDataPart("mask", "mask", maskBody);
    }

    return api.createImageEdit(builder.build());
  }

  public ImageResult createImageVariation(CreateImageVariationRequest request, String imagePath) {
    java.io.File image = new java.io.File(imagePath);
    return createImageVariation(request, image);
  }

  public ListenableFuture<ImageResult> createImageVariationAsync(
      CreateImageVariationRequest request, String imagePath) {
    java.io.File image = new java.io.File(imagePath);
    return createImageVariationAsync(request, image);
  }

  public ImageResult createImageVariation(CreateImageVariationRequest request, java.io.File image) {
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

    return execute(api.createImageVariation(builder.build()));
  }

  public ListenableFuture<ImageResult> createImageVariationAsync(
      CreateImageVariationRequest request, java.io.File image) {
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

    return api.createImageVariation(builder.build());
  }

  public ModerationResult createModeration(ModerationRequest request) {
    return execute(api.createModeration(request));
  }

  public ListenableFuture<ModerationResult> createModerationAsync(ModerationRequest request) {
    return api.createModeration(request);
  }
}
