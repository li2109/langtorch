package ai.knowly.llm.openai;

import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.llm.base.chatmodel.BaseChatModel;
import ai.knowly.llm.message.AssistantMessage;
import ai.knowly.llm.message.BaseChatMessage;
import ai.knowly.llm.message.Role;
import ai.knowly.llm.message.SystemMessage;
import ai.knowly.llm.message.UserMessage;
import com.google.common.collect.ImmutableList;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestBuilder;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import javax.inject.Inject;

/** OpenAIChat is a chat model that uses the OpenAI API to generate text. */
public final class OpenAIChat extends BaseChatModel {
  private final OpenAiService openAiService;
  private final ChatCompletionRequestBuilder completionRequest;

  @Inject
  OpenAIChat(OpenAiService openAiService) {
    this.openAiService = openAiService;
    this.completionRequest = ChatCompletionRequest.builder().maxTokens(2048);
  }

  private static ChatMessage toChatMessage(BaseChatMessage message) {
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setContent(message.returnMessage());
    chatMessage.setRole(message.returnRole().name().toLowerCase());
    return chatMessage;
  }

  public OpenAIChat setMaxTokens(int maxTokens) {
    completionRequest.maxTokens(maxTokens);
    return this;
  }

  public OpenAIChat setModel(String model) {
    completionRequest.model(model);
    return this;
  }

  public OpenAIChat setTemperature(double temperature) {
    completionRequest.temperature(temperature);
    return this;
  }

  @Override
  public BaseChatMessage run(List<BaseChatMessage> messages) {
    ChatCompletionResult completion =
        openAiService.createChatCompletion(
            completionRequest
                .messages(
                    messages.stream().map(OpenAIChat::toChatMessage).collect(toImmutableList()))
                .build());
    ChatMessage chatMessage = completion.getChoices().get(0).getMessage();
    if (Role.USER.name().toLowerCase().equals(chatMessage.getRole())) {
      return new UserMessage(chatMessage.getContent());
    }
    if (Role.SYSTEM.name().toLowerCase().equals(chatMessage.getRole())) {
      return new SystemMessage(chatMessage.getContent());
    }
    if (Role.ASSISTANT.name().toLowerCase().equals(chatMessage.getRole())) {
      return new AssistantMessage(chatMessage.getContent());
    }
    throw new RuntimeException(
        String.format(
            "Unknown role %s with message: %s ", chatMessage.getRole(), chatMessage.getContent()));
  }

  @Override
  public String run(String message) {
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setRole(Role.USER.name().toLowerCase());
    chatMessage.setContent(message);
    ChatCompletionResult completion =
        openAiService.createChatCompletion(
            completionRequest.messages(ImmutableList.of(chatMessage)).build());

    return completion.getChoices().get(0).getMessage().getContent();
  }
}
