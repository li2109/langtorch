package ai.knowly.langtoch.llm.integration.openai.service;

import ai.knowly.langtoch.llm.integration.openai.service.schema.config.OpenAIProxyConfig;
import ai.knowly.langtoch.llm.integration.openai.service.schema.config.OpenAIServiceConfig;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.OpenAIError;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.OpenAIHttpException;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion.CompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion.CompletionResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.edit.EditRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.edit.EditResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageEditRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageVariationRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.ImageResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.moderation.ModerationRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.moderation.ModerationResult;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OpenAIService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.openai.com/";
  private static final ObjectMapper mapper = defaultObjectMapper();

  private final OpenAIApi api;
  private final ExecutorService executorService;

  /**
   * Creates a new OpenAiService that wraps OpenAiApi
   *
   * @param token OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
   */
  public OpenAIService(final String token) {
    this(OpenAIServiceConfig.builder().setApiKey(token).build());
  }

  /**
   * Creates a new OpenAiService that wraps OpenAiApi
   *
   * @param token OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
   * @param timeout http read timeout, Duration.ZERO means no timeout
   */
  public OpenAIService(final String token, final Duration timeout) {
    this(OpenAIServiceConfig.builder().setApiKey(token).setTimeoutDuration(timeout).build());
  }

  public OpenAIService(final OpenAIServiceConfig openAIServiceConfig) {
    ObjectMapper mapper = defaultObjectMapper();
    if (openAIServiceConfig.proxyConfig().isPresent()) {}
    OkHttpClient client = buildClient(openAIServiceConfig);
    Retrofit retrofit = defaultRetrofit(client, mapper);

    this.api = retrofit.create(OpenAIApi.class);
    this.executorService = client.dispatcher().executorService();
  }

  /**
   * Creates a new OpenAiService that wraps OpenAiApi. Use this if you need more customization, but
   * use OpenAiService(api, executorService) if you use streaming and want to shut down instantly
   *
   * @param api OpenAiApi instance to use for all methods
   */
  public OpenAIService(final OpenAIApi api) {
    this.api = api;
    this.executorService = null;
  }

  /**
   * Creates a new OpenAiService that wraps OpenAiApi. The ExecutorService must be the one you get
   * from the client you created the api with otherwise shutdownExecutor() won't work.
   *
   * <p>Use this if you need more customization.
   *
   * @param api OpenAiApi instance to use for all methods
   * @param executorService the ExecutorService from client.dispatcher().executorService()
   */
  public OpenAIService(final OpenAIApi api, final ExecutorService executorService) {
    this.api = api;
    this.executorService = executorService;
  }

  /** Calls the Open AI api, returns the response, and parses error messages if the request fails */
  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
          OpenAIError error = mapper.readValue(errorBody, OpenAIError.class);
          throw new OpenAIHttpException(error, e, httpException.code());
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }
      throw new RuntimeException(e);
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

  private static Proxy.Type convertProxyEnum(OpenAIProxyConfig.ProxyType proxyType) {
    if (proxyType == OpenAIProxyConfig.ProxyType.HTTP) {
      return Proxy.Type.HTTP;
    } else if (proxyType == OpenAIProxyConfig.ProxyType.SOCKS) {
      return Proxy.Type.SOCKS;
    } else {
      throw new IllegalArgumentException("Unknown proxy type: " + proxyType);
    }
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
    RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MediaType.get("multipart/form-data"))
            .addFormDataPart("prompt", request.getPrompt())
            .addFormDataPart("size", request.getSize())
            .addFormDataPart("response_format", request.getResponseFormat())
            .addFormDataPart("image", "image", imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(MediaType.parse("image"), mask);
      builder.addFormDataPart("mask", "mask", maskBody);
    }

    return execute(api.createImageEdit(builder.build()));
  }

  public ListenableFuture<ImageResult> createImageEditAsync(
      CreateImageEditRequest request, java.io.File image, java.io.File mask) {
    RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MediaType.get("multipart/form-data"))
            .addFormDataPart("prompt", request.getPrompt())
            .addFormDataPart("size", request.getSize())
            .addFormDataPart("response_format", request.getResponseFormat())
            .addFormDataPart("image", "image", imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    if (mask != null) {
      RequestBody maskBody = RequestBody.create(MediaType.parse("image"), mask);
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
    RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MediaType.get("multipart/form-data"))
            .addFormDataPart("size", request.getSize())
            .addFormDataPart("response_format", request.getResponseFormat())
            .addFormDataPart("image", "image", imageBody);

    if (request.getN() != null) {
      builder.addFormDataPart("n", request.getN().toString());
    }

    return execute(api.createImageVariation(builder.build()));
  }

  public ListenableFuture<ImageResult> createImageVariationAsync(
      CreateImageVariationRequest request, java.io.File image) {
    RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

    MultipartBody.Builder builder =
        new MultipartBody.Builder()
            .setType(MediaType.get("multipart/form-data"))
            .addFormDataPart("size", request.getSize())
            .addFormDataPart("response_format", request.getResponseFormat())
            .addFormDataPart("image", "image", imageBody);

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

  /**
   * Shuts down the OkHttp ExecutorService. The default behaviour of OkHttp's ExecutorService
   * (ConnectionPool) is to shut down after an idle timeout of 60s. Call this method to shut down
   * the ExecutorService immediately.
   */
  public void shutdownExecutor() {
    Objects.requireNonNull(
        this.executorService, "executorService must be set in order to shut down");
    this.executorService.shutdown();
  }
}
