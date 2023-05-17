package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionChoice;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessage;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessageRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ChatCompletionTest {

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createChatCompletion() {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
    final List<ChatMessage> messages = new ArrayList<>();
    final ChatMessage systemMessage =
        new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
    messages.add(systemMessage);

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(messages)
            .n(5)
            .maxTokens(50)
            .logitBias(new HashMap<>())
            .build();

    List<ChatCompletionChoice> choices =
        service.createChatCompletion(chatCompletionRequest).getChoices();
    assertEquals(5, choices.size());
  }
}
