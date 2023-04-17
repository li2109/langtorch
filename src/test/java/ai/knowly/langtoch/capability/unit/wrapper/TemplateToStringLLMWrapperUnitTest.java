package ai.knowly.langtoch.capability.unit.wrapper;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.providers.openai.OpenAIChat;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemplateToStringLLMWrapperUnitTest {
  @Mock OpenAIChat openAIChat;

  @Test
  public void testSimpleLLVMChain_defaultDirectOutputParser() {
    // Arrange.
    when(openAIChat.run(anyString())).thenReturn("Google");

    PromptTemplate promptTemplate =
        PromptTemplate.builder()
            .setTemplate("Write a creative name for a {{$area}} company.")
            .addVariableValuePair("area", "search engine")
            .build();

    TemplateToStringLLMWrapperUnit templateToStringLLMWrapperUnit =
        new TemplateToStringLLMWrapperUnit(openAIChat);

    // Act.
    String result = templateToStringLLMWrapperUnit.run(promptTemplate);

    // Assert.
    assertThat(result).isEqualTo("Google");
    verify(openAIChat).run("Write a creative name for a search engine company.");
  }
}
