package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionResult;
import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtoch.schema.chat.AssistantMessage;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import ai.knowly.langtoch.schema.chat.SystemMessage;
import ai.knowly.langtoch.schema.chat.UserMessage;
import ai.knowly.langtoch.schema.io.MultiChatMessage;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
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
  private final OpenAIService openAIService;
  // Configuration for the OpenAI Chat Processor
  private OpenAIChatProcessorConfig openAIChatProcessorConfig =
      OpenAIChatProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKEN)
          .build();

  // Constructor with dependency injection
  @Inject
  OpenAIChatProcessor(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }

  // Private constructor used in factory methods
  private OpenAIChatProcessor() {
    this.openAIService = OpenAIServiceProvider.createOpenAIService();
  }

  public static OpenAIChatProcessor create(String openAIKey) {
    return new OpenAIChatProcessor(OpenAIServiceProvider.createOpenAIService(openAIKey));
  }

  // Factory method to create a new OpenAIChatProcessor instance
  public static OpenAIChatProcessor create() {
    return new OpenAIChatProcessor();
  }

  // Factory method to create a new OpenAIChatProcessor instance with a given OpenAiApi instance
  public static OpenAIChatProcessor create(OpenAIService openAIService) {
    return new OpenAIChatProcessor(openAIService);
  }

  // Method to set the processor configuration
  public OpenAIChatProcessor withConfig(OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    this.openAIChatProcessorConfig = openAIChatProcessorConfig;
    return this;
  }

  // Method to run the processor with the given input and return the output chat message
  @Override
  public ChatMessage run(MultiChatMessage inputData) {
    ChatCompletionRequest chatCompletionRequest =
        OpenAIChatProcessorRequestConverter.convert(
            openAIChatProcessorConfig, inputData.getMessages());
    ChatCompletionResult chatCompletion = openAIService.createChatCompletion(chatCompletionRequest);
    ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessage
        chatMessage = chatCompletion.getChoices().get(0).getMessage();
    if (Role.USER.name().toLowerCase().equals(chatMessage.getRole())) {
      return UserMessage.builder().setMessage(chatMessage.getContent()).build();
    }
    if (Role.SYSTEM.name().toLowerCase().equals(chatMessage.getRole())) {
      return SystemMessage.builder().setMessage(chatMessage.getContent()).build();
    }
    if (Role.ASSISTANT.name().toLowerCase().equals(chatMessage.getRole())) {
      return AssistantMessage.builder().setMessage(chatMessage.getContent()).build();
    }
    throw new RuntimeException(
        String.format(
            "Unknown role %s with message: %s ", chatMessage.getRole(), chatMessage.getContent()));
  }

  @Override
  public ListenableFuture<ChatMessage> runAsync(MultiChatMessage inputData) {
    ChatCompletionRequest chatCompletionRequest =
        OpenAIChatProcessorRequestConverter.convert(
            openAIChatProcessorConfig, inputData.getMessages());
    ListenableFuture<ChatCompletionResult> chatCompletionAsync =
        openAIService.createChatCompletionAsync(chatCompletionRequest);
    return FluentFuture.from(chatCompletionAsync)
        .transform(
            chatCompletion -> {
              ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessage
                  chatMessage = chatCompletion.getChoices().get(0).getMessage();
              if (Role.USER.name().toLowerCase().equals(chatMessage.getRole())) {
                return UserMessage.builder().setMessage(chatMessage.getContent()).build();
              }
              if (Role.SYSTEM.name().toLowerCase().equals(chatMessage.getRole())) {
                return SystemMessage.builder().setMessage(chatMessage.getContent()).build();
              }
              if (Role.ASSISTANT.name().toLowerCase().equals(chatMessage.getRole())) {
                return AssistantMessage.builder().setMessage(chatMessage.getContent()).build();
              }
              throw new RuntimeException(
                  String.format(
                      "Unknown role %s with message: %s ",
                      chatMessage.getRole(), chatMessage.getContent()));
            },
            directExecutor());
  }
}
