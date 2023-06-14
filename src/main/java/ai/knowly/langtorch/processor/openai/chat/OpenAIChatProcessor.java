package ai.knowly.langtorch.processor.module.openai.chat;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.chat.AssistantMessage;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.Role;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import ai.knowly.langtorch.schema.chat.UserMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

/**
 * OpenAI chat module implementation. Handles chat input and output for the OpenAI Language Model.
 */
public class OpenAIChatProcessor implements Processor<MultiChatMessage, ChatMessage> {
  // OpenAiApi instance used for making requests
  private final OpenAIService openAIService;
  // Configuration for the OpenAI Chat Processor
  private final OpenAIChatProcessorConfig openAIChatProcessorConfig;

  @Inject
  public OpenAIChatProcessor(
      OpenAIService openAIService, OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    this.openAIService = openAIService;
    this.openAIChatProcessorConfig = openAIChatProcessorConfig;
  }

  // Method to run the module with the given input and return the output chat message
  @Override
  public ChatMessage run(MultiChatMessage inputData) {
    ChatCompletionRequest chatCompletionRequest =
        OpenAIChatProcessorRequestConverter.convert(
            openAIChatProcessorConfig, inputData.getMessages());
    ChatCompletionResult chatCompletion = openAIService.createChatCompletion(chatCompletionRequest);
    ChatMessage chatMessage = chatCompletion.getChoices().get(0).getMessage();
    if (Role.USER == chatMessage.getRole()) {
      return UserMessage.of(chatMessage.getContent());
    }
    if (Role.SYSTEM == chatMessage.getRole()) {
      return SystemMessage.of(chatMessage.getContent());
    }
    if (Role.ASSISTANT == chatMessage.getRole()) {
      return AssistantMessage.of(chatMessage.getContent());
    }
    throw new UnknownMessageException(
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
              ChatMessage chatMessage = chatCompletion.getChoices().get(0).getMessage();
              if (chatMessage.getRole() == Role.USER) {
                return UserMessage.of(chatMessage.getContent());
              }
              if (chatMessage.getRole() == Role.SYSTEM) {
                return SystemMessage.of(chatMessage.getContent());
              }
              if (chatMessage.getRole() == Role.ASSISTANT) {
                return AssistantMessage.of(chatMessage.getContent());
              }
              throw new UnknownMessageException(
                  String.format(
                      "Unknown role %s with message: %s ",
                      chatMessage.getRole(), chatMessage.getContent()));
            },
            directExecutor());
  }
}
