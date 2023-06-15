package ai.knowly.langtorch.llm.minimax;

import static ai.knowly.langtorch.util.MiniMaxServiceTestingUtils.MINIMAX_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableMiniMaxLLMServiceLiveTrafficTest")
class ChatCompletionTest {

  @Test
  void createChatCompletion() {
    MiniMaxService service = MINIMAX_TESTING_SERVICE;
    final List<ChatCompletionRequest.Message> messages = new ArrayList<>();
    messages.add(
        ChatCompletionRequest.Message.builder()
            .setSenderType("USER")
            .setText("Hello, how are you?")
            .build());

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder().setModel("abab5-chat").setMessages(messages).build();

    String reply = service.createChatCompletion(chatCompletionRequest).getReply();
    assertNotNull(reply);
  }
}
