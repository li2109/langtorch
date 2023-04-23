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

/** A chat model is a model that takes in a list of messages and returns a message. */
public class ChatProcessor implements Processor<MultiChatMessageInput, ChatMessage> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final OpenAiService openAiService;
  private ChatProcessorConfig chatProcessorConfig = ChatProcessorConfig.builder().build();

  @Inject
  ChatProcessor(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  private ChatProcessor() {
    this.openAiService = new OpenAiService(getOpenAIApiKeyFromEnv(logger));
  }

  public static ChatProcessor create() {
    return new ChatProcessor();
  }

  public static ChatProcessor create(OpenAiService openAiService) {
    return new ChatProcessor(openAiService);
  }

  public ChatProcessor withConfig(ChatProcessorConfig chatProcessorConfig) {
    this.chatProcessorConfig = chatProcessorConfig;
    return this;
  }

  @Override
  public ChatMessage run(MultiChatMessageInput inputData) {
    ChatCompletionRequest chatCompletionRequest =
        ChatProcessorRequestConverter.convert(chatProcessorConfig, inputData.getMessages());

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
