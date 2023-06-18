package ai.knowly.langtorch.llm.minimax;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.schema.chat.Role;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableMiniMaxLLMServiceLiveTrafficTest")
class ChatCompletionTest {

  @Inject private MiniMaxService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new MiniMaxServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
  void createChatCompletion() {
    final List<ChatCompletionRequest.Message> messages = new ArrayList<>();
    messages.add(
        ChatCompletionRequest.Message.builder()
            .setSenderType(Role.MINIMAX_USER.toString())
            .setText("Hello, how are you?")
            .build());

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder().setModel("abab5-chat").setMessages(messages).build();

    String reply = service.createChatCompletion(chatCompletionRequest).getReply();
    assertThat(reply).isNotEmpty();
  }
}
