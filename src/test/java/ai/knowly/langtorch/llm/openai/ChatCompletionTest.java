package ai.knowly.langtorch.llm.openai;

import static ai.knowly.langtorch.util.OpenAIServiceTestingUtils.OPENAI_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionChoice;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class ChatCompletionTest {

  @Test
  void createChatCompletion() {
    OpenAIService service = OPENAI_TESTING_SERVICE;
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
