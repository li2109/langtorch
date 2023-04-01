package ai.knowly.chain;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.llm.openai.OpenAIChat;
import ai.knowly.llm.openai.OpenAIServiceModule;
import ai.knowly.prompt.PromptTemplate;
import com.google.acai.Acai;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LLMChainTest {
  @Rule public Acai acai = new Acai(MyTestModule.class);
  @Inject OpenAIChat openAIChat;

  @Test
  public void testSimpleLLMChain() {
    // Arrange.
    OpenAIChat model = openAIChat.setModel("gpt-3.5-turbo").setTemperature(0);
    PromptTemplate promptTemplate =
        PromptTemplate.builder().setTemplate("Write a creative name for a {area} company.").build();
    // Act.
    LLMChain chain = new LLMChain(model, promptTemplate.addVariable("area", "search engine"));
    String result = chain.run();
    System.out.println(result);

    // Assert.
    assertThat(result).isNotEmpty();
  }

  private static class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      install(new OpenAIServiceModule());
    }
  }
}
