package ai.knowly.langtoch.llm.openai;

import static ai.knowly.langtoch.llm.openai.Utils.getApiKeyFromEnv;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtoch.llm.base.chatmodel.BaseChatModel;
import ai.knowly.langtoch.llm.message.AssistantMessage;
import ai.knowly.langtoch.llm.message.BaseChatMessage;
import ai.knowly.langtoch.llm.message.Role;
import ai.knowly.langtoch.llm.message.SystemMessage;
import ai.knowly.langtoch.llm.message.UserMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestBuilder;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import javax.inject.Inject;

/** OpenAIChat is a chat model that uses the OpenAI API to generate text. */
public class OpenAIChat extends BaseChatModel {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final int DEFAULT_MAX_TOKENS = 2048;
  private final String DEFAULT_MODEL = "gpt-3.5-turbo";

  private final OpenAiService openAiService;
  private final ChatCompletionRequestBuilder completionRequest =
      ChatCompletionRequest.builder().maxTokens(DEFAULT_MAX_TOKENS).model(DEFAULT_MODEL);

  @Inject
  OpenAIChat(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  public OpenAIChat(String apiKey) {
    Utils.logPartialApiKey(logger, apiKey);
    this.openAiService = new OpenAiService(apiKey);
  }

  public OpenAIChat() {
    this.openAiService = new OpenAiService(getApiKeyFromEnv(logger));
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
