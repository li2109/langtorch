package ai.knowly.langtoch.llm.processor.openai.chat;

import static ai.knowly.langtoch.llm.Utils.singleToCompletableFuture;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtoch.llm.schema.chat.AssistantMessage;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.chat.SystemMessage;
import ai.knowly.langtoch.llm.schema.chat.UserMessage;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;

/**
 * OpenAI chat processor implementation. Handles chat input and output for the OpenAI Language
 * Model.
 */
public class OpenAIChatProcessor implements Processor<MultiChatMessage, ChatMessage> {
  // Logger, default model, and default max tokens for this processor
  private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
  private static final int DEFAULT_MAX_TOKEN = 2048;
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  // OpenAiApi instance used for making requests
  private final OpenAiApi openAiApi;
  // Configuration for the OpenAI Chat Processor
  private OpenAIChatProcessorConfig openAIChatProcessorConfig =
      OpenAIChatProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKEN)
          .build();

  // Constructor with dependency injection
  @Inject
  OpenAIChatProcessor(OpenAiApi openAiApi) {
    this.openAiApi = openAiApi;
  }

  // Private constructor used in factory methods
  private OpenAIChatProcessor() {
    this.openAiApi = OpenAIServiceProvider.createOpenAiAPI();
  }

  public static OpenAIChatProcessor create(String openAIKey) {
    return new OpenAIChatProcessor(OpenAIServiceProvider.createOpenAiAPI(openAIKey));
  }

  // Factory method to create a new OpenAIChatProcessor instance
  public static OpenAIChatProcessor create() {
    return new OpenAIChatProcessor();
  }

  // Factory method to create a new OpenAIChatProcessor instance with a given OpenAiApi instance
  public static OpenAIChatProcessor create(OpenAiApi openAiApi) {
    return new OpenAIChatProcessor(openAiApi);
  }

  // Method to set the processor configuration
  public OpenAIChatProcessor withConfig(OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    this.openAIChatProcessorConfig = openAIChatProcessorConfig;
    return this;
  }

  // Method to run the processor with the given input and return the output chat message
  @Override
  public ChatMessage run(MultiChatMessage inputData) {
    try {
      return runAsync(CompletableFuture.completedFuture(inputData)).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.atWarning().withCause(e).log(
          "Error running OpenAIChatProcessor with input: %s", inputData);
      throw new RuntimeException(e);
    }
  }

  @Override
  public CompletableFuture<ChatMessage> runAsync(CompletableFuture<MultiChatMessage> inputData) {
    return inputData.thenCompose(
        data -> {
          ChatCompletionRequest chatCompletionRequest =
              OpenAIChatProcessorRequestConverter.convert(
                  openAIChatProcessorConfig, data.getMessages());

          return singleToCompletableFuture(openAiApi.createChatCompletion(chatCompletionRequest))
              .thenApply(
                  chatCompletion -> {
                    com.theokanning.openai.completion.chat.ChatMessage chatMessage =
                        chatCompletion.getChoices().get(0).getMessage();
                    if (Role.USER.name().toLowerCase().equals(chatMessage.getRole())) {
                      return UserMessage.builder().setMessage(chatMessage.getContent()).build();
                    }
                    if (Role.SYSTEM.name().toLowerCase().equals(chatMessage.getRole())) {
                      return SystemMessage.builder().setMessage(chatMessage.getContent()).build();
                    }
                    if (Role.ASSISTANT.name().toLowerCase().equals(chatMessage.getRole())) {
                      return AssistantMessage.builder()
                          .setMessage(chatMessage.getContent())
                          .build();
                    }
                    throw new RuntimeException(
                        String.format(
                            "Unknown role %s with message: %s ",
                            chatMessage.getRole(), chatMessage.getContent()));
                  });
        });
  }
}
