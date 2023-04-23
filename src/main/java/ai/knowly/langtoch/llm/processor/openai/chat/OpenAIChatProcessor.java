package ai.knowly.langtoch.llm.processor.openai.chat;

import static ai.knowly.langtoch.llm.Utils.getOpenAIApiKeyFromEnv;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.chat.AssistantMessage;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.chat.SystemMessage;
import ai.knowly.langtoch.llm.schema.chat.UserMessage;
import ai.knowly.langtoch.llm.schema.io.input.MultiChatMessageInput;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import javax.inject.Inject;

/**
 * OpenAI chat processor implementation. Handles chat input and output for the OpenAI Language
 * Model.
 */
public class OpenAIChatProcessor implements Processor<MultiChatMessageInput, ChatMessage> {
  // Logger, default model, and default max tokens for this processor
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
  private static final int DEFAULT_MAX_TOKEN = 2048;

  // OpenAiService instance used for making requests
  private final OpenAiService openAiService;
  // Configuration for the OpenAI Chat Processor
  private OpenAIChatProcessorConfig openAIChatProcessorConfig =
      OpenAIChatProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKEN)
          .build();

  // Constructor with dependency injection
  @Inject
  OpenAIChatProcessor(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  // Private constructor used in factory methods
  private OpenAIChatProcessor() {
    this.openAiService = new OpenAiService(getOpenAIApiKeyFromEnv(logger));
  }

  // Factory method to create a new OpenAIChatProcessor instance
  public static OpenAIChatProcessor create() {
    return new OpenAIChatProcessor();
  }

  // Factory method to create a new OpenAIChatProcessor instance with a given OpenAiService instance
  public static OpenAIChatProcessor create(OpenAiService openAiService) {
    return new OpenAIChatProcessor(openAiService);
  }

  // Method to set the processor configuration
  public OpenAIChatProcessor withConfig(OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    this.openAIChatProcessorConfig = openAIChatProcessorConfig;
    return this;
  }

  // Method to run the processor with the given input and return the output chat message
  @Override
  public ChatMessage run(MultiChatMessageInput inputData) {
    ChatCompletionRequest chatCompletionRequest =
        OpenAIChatProcessorRequestConverter.convert(
            openAIChatProcessorConfig, inputData.getMessages());

    ChatCompletionResult chatCompletion = openAiService.createChatCompletion(chatCompletionRequest);
    com.theokanning.openai.completion.chat.ChatMessage chatMessage =
        chatCompletion.getChoices().get(0).getMessage();
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
}
