package ai.knowly.langtoch.capability.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.providers.openai.OpenAIChat;
import ai.knowly.langtoch.prompt.PromptTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleLLMCapabilityUnitTest {
  @Mock OpenAIChat openAIChat;

  @Test
  public void testSimpleLLMChain_defualtDirectOutputParser() {
    // Arrange.
    when(openAIChat.run(anyString())).thenReturn("Google");

    PromptTemplate promptTemplate =
        PromptTemplate.builder()
            .setTemplate("Write a creative name for a {{$area}} company.")
            .addVariableValuePair("area", "search engine")
            .build();

    SimpleLLMCapabilityUnit simpleLLMCapabilityUnit = new SimpleLLMCapabilityUnit(openAIChat);

    // Act.
    String result = simpleLLMCapabilityUnit.run(promptTemplate);

    // Assert.
    assertThat(result).isEqualTo("Google");
    verify(openAIChat).run("Write a creative name for a search engine company.");
  }
}