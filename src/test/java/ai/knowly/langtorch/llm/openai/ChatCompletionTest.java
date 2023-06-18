package ai.knowly.langtorch.llm.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.module.token.TokenUsage;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.Function;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.Parameters;
import ai.knowly.langtorch.schema.chat.SystemMessage;
import ai.knowly.langtorch.schema.chat.UserMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.HashMap;
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
    // Arrange.
    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .setModel("gpt-3.5-turbo")
            .setMessages(
                ImmutableList.of(SystemMessage.of("You are a dog and will speak as such.")))
            .setN(3)
            .setMaxTokens(50)
            .setLogitBias(new HashMap<>())
            .build();

    // Act.
    ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);

    // Assert.
    assertThat(result.getChoices().size()).isEqualTo(3);
    assertThat(result.getUsage().getCompletionTokens())
        .isEqualTo(tokenUsage.getCompletionTokenUsage().get());
    assertThat(result.getUsage().getPromptTokens())
        .isEqualTo(tokenUsage.getPromptTokenUsage().get());
  }

  @Test
  void createChatCompletion_testFunctionCall() {
    // Arrange.
    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .setModel("gpt-3.5-turbo-0613")
            .setMessages(ImmutableList.of(UserMessage.of("What's the weather like in Boston?")))
            .setFunctions(
                ImmutableList.of(
                    Function.builder()
                        .setName("get_current_weather")
                        .setDescription("Get the current weather in a given location")
                        .setParameters(
                            Parameters.builder()
                                .setRequired(ImmutableList.of("location"))
                                .setProperties(
                                    ImmutableMap.<String, Object>builder()
                                        .put(
                                            "location",
                                            ImmutableMap.builder()
                                                .put("type", "string")
                                                .put(
                                                    "description",
                                                    "The city and state, e.g. San Francisco, CA")
                                                .build())
                                        .put(
                                            "unit",
                                            ImmutableMap.builder()
                                                .put("type", "string")
                                                .put(
                                                    "enum",
                                                    ImmutableList.of("celsius", "fahrenheit"))
                                                .build())
                                        .build())
                                .setType("object")
                                .build())
                        .build()))
            .setFunctionCall("auto")
            .setN(1)
            .setMaxTokens(50)
            .setLogitBias(new HashMap<>())
            .build();

    // Act.
    ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);

    // Assert.
    assertThat(result.getChoices().size()).isEqualTo(1);
    assertThat(result.getChoices().get(0).getFinishReason()).isEqualTo("function_call");
    assertThat(result.getChoices().get(0).getMessage().getFunctionCall()).isNotNull();
  }

  @Test
  void createChatCompletion_testFunctionCallWithSql() {
    // Arrange.
    ChatCompletionRequest chatCompletionRequest =
        ChatCompletionRequest.builder()
            .setModel("gpt-3.5-turbo-0613")
            .setMessages(
                ImmutableList.of(
                    SystemMessage.of(
                        "Answer user questions by generating SQL queries against the Chinook Music Database."),
                    UserMessage.of("Hi, who are the top 5 artists by number of tracks?")))
            .setFunctions(
                ImmutableList.of(
                    Function.builder()
                        .setName("ask_database")
                        .setDescription(
                            "Use this function to answer user questions about music. Output should be a fully formed SQL query.")
                        .setParameters(
                            Parameters.builder()
                                .setType("object")
                                .setProperties(
                                    ImmutableMap.<String, Object>builder()
                                        .put(
                                            "query",
                                            ImmutableMap.builder()
                                                .put("type", "string")
                                                .put(
                                                    "description",
                                                    "SQL query extracting info to answer the user's question.\\nSQL should be written using this database schema:\\n{database_schema_string}\\nThe query should be returned in plain text, not in JSON.\\n")
                                                .build())
                                        .build())
                                .setRequired(ImmutableList.of("query"))
                                .build())
                        .build()))
            .setFunctionCall("auto")
            .setN(1)
            .setMaxTokens(50)
            .setLogitBias(new HashMap<>())
            .build();

    // Act.
    ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);

    // Assert.
    assertThat(result.getChoices().size()).isEqualTo(1);
    assertThat(result.getChoices().get(0).getMessage().getFunctionCall()).isNotNull();
  }
}
