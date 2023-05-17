package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAiService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionChoice;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionChunk;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessage;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessageRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ChatCompletionTest {

  String token = Utils.getOpenAIApiKeyFromEnv();
  OpenAiService service = new OpenAiService(token);

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createChatCompletion() {
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

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void streamChatCompletion() {
    final List<ChatMessage> messages = new ArrayList<>();
    final ChatMessage systemMessage =
        new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
    messages.add(systemMessage);

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(messages)
            .n(1)
            .maxTokens(50)
            .logitBias(new HashMap<>())
            .stream(true)
            .build();

    List<ChatCompletionChunk> chunks = new ArrayList<>();
    service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
    assertTrue(chunks.size() > 0);
    assertNotNull(chunks.get(0).getChoices().get(0));
  }
}
