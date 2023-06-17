package ai.knowly.langtorch.llm.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.module.token.TokenUsage;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class ChatCompletionTest {
  @Inject private OpenAIService service;
  @Inject private TokenUsage tokenUsage;

  @BeforeEach
  void setUp() {
    Guice.createInjector(
            BoundFieldModule.of(this),
            new OpenAIServiceConfigTestingModule(),
            new OpenAITokenModule())
        .injectMembers(this);
  }

  @Test
  void createChatCompletion() {
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

    ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
    assertThat(result.getChoices().size()).isEqualTo(3);
    assertThat(result.getUsage().getCompletionTokens())
        .isEqualTo(tokenUsage.getCompletionTokenUsage().get());
    assertThat(result.getUsage().getPromptTokens())
        .isEqualTo(tokenUsage.getPromptTokenUsage().get());
  }
}
