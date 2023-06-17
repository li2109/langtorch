package ai.knowly.langtorch.llm.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.module.token.TokenUsage;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.Function;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.Parameters;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import ai.knowly.langtorch.schema.chat.UserMessage;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Test
  void createChatCompletion_testFunctionCall() {
    final List<ChatMessage> messages = new ArrayList<>();
    messages.add(UserMessage.of("What's the weather like in Boston?"));

    Map<String, String> locationMap = new HashMap<>();
    locationMap.put("type", "string");
    locationMap.put("description", "The city and state, e.g. San Francisco, CA");
    Map<String, Object> unitMap = new HashMap<>();
    unitMap.put("type", "string");
    unitMap.put("enum", ImmutableList.of("celsius", "fahrenheit"));
    Map<String, Object> propertiesMap = new HashMap<>();
    propertiesMap.put("location", locationMap);
    propertiesMap.put("unit", unitMap);

    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .setModel("gpt-3.5-turbo-0613")
            .setMessages(messages)
            .setFunctions(
                ImmutableList.of(
                    Function.builder()
                        .setName("get_current_weather")
                        .setDescription("Get the current weather in a given location")
                        .setParameters(
                            Parameters.builder()
                                .setRequired(ImmutableList.of("location"))
                                .setProperties(propertiesMap)
                                .setType("object")
                                .build())
                        .build()))
            .setFunctionCall("auto")
            .setN(1)
            .setMaxTokens(50)
            .setLogitBias(new HashMap<>())
            .build();

    ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
    assertThat(result.getChoices().size()).isEqualTo(1);
    assertThat(result.getChoices().get(0).getFinishReason()).isEqualTo("function_call");
    assertThat(result.getChoices().get(0).getMessage().getFunctionCall()).isNotNull();
  }
}
