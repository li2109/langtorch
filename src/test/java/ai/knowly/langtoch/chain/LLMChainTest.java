package ai.knowly.langtoch.chain;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.openai.OpenAIChat;
import ai.knowly.langtoch.prompt.PromptTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LLMChainTest {
  @Mock OpenAIChat openAIChat;

  @Test
  public void testSimpleLLMChain() {
    // Arrange.
    when(openAIChat.run(anyString())).thenReturn("Google");

    PromptTemplate promptTemplate =
        PromptTemplate.builder()
            .setTemplate("Write a creative name for a {{$area}} company.")
            .build();
    // Act.
    LLMChain chain =
        new LLMChain(openAIChat, promptTemplate.addVariableValuePair("area", "search engine"));
    String result = chain.run();

    // Assert.
    assertThat(result).isEqualTo("Google");
    verify(openAIChat).run("Write a creative name for a search engine company.");
  }
}
