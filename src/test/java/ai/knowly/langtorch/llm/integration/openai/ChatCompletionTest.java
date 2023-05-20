package ai.knowly.langtorch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.llm.Utils;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.completion.chat.ChatCompletionChoice;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ChatCompletionTest {

  @Test
  @EnabledIf("ai.knowly.langtorch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createChatCompletion() {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
    final List<ChatMessage> messages = new ArrayList<>();
    messages.add(SystemMessage.of("You are a dog and will speak as such."));

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .setModel("gpt-3.5-turbo")
            .setMessages(messages)
            .setN(3)
            .setMaxTokens(50)
            .setLogitBias(new HashMap<>())
            .build();

    List<ChatCompletionChoice> choices =
        service.createChatCompletion(chatCompletionRequest).getChoices();
    assertEquals(3, choices.size());
  }
}
