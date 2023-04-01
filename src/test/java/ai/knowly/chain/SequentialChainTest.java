package ai.knowly.chain;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.llm.openai.OpenAIChat;
import ai.knowly.llm.openai.OpenAIServiceModule;
import ai.knowly.prompt.PromptTemplate;
import com.google.acai.Acai;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SequentialChainTest {
  @Rule public Acai acai = new Acai(MyTestModule.class);
  @Inject OpenAIChat openAIChat;

  @Test
  public void testSequentialMChain() {
    // Arrange.
    OpenAIChat model = openAIChat.setModel("gpt-3.5-turbo").setTemperature(0);
    PromptTemplate promptTemplate1 =
        PromptTemplate.builder()
            .setTemplate("Write a creative company name if the product is {product}.")
            .build();
    PromptTemplate promptTemplate2 =
        PromptTemplate.builder()
            .setTemplate("Write a slogan for a {company name} company.")
            .build();
    LLMChain chain1 = new LLMChain(model, promptTemplate1);
    LLMChain chain2 = new LLMChain(model, promptTemplate2);
    SequentialChain sequentialChain =
        SequentialChain.builder().setChains(ImmutableList.of(chain1, chain2)).build();

    // Act.
    String result = sequentialChain.run("Motorcycle");
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
