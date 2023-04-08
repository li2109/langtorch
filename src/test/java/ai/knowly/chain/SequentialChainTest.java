package ai.knowly.chain;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.llm.openai.OpenAIChat;
import ai.knowly.prompt.PromptTemplate;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SequentialChainTest {
  @Mock OpenAIChat openAIChat;

  @Test
  public void testSequentialMChain() {
    // Arrange.
    String prompt1 = "Write a creative company name if the product is Motorcycle.";
    String companyName = "Awesome Motorcycle";
    String prompt2 = "Write a slogan for a Awesome Motorcycle company.";
    String slogan = "Awesome! Motorcycle!";

    when(openAIChat.run(prompt1)).thenReturn(companyName);
    when(openAIChat.run(prompt2)).thenReturn(slogan);

    PromptTemplate promptTemplate1 =
        PromptTemplate.builder()
            .setTemplate("Write a creative company name if the product is {{$product}}.")
            .build();
    PromptTemplate promptTemplate2 =
        PromptTemplate.builder()
            .setTemplate("Write a slogan for a {{$company_name}} company.")
            .build();

    LLMChain chain1 = new LLMChain(openAIChat, promptTemplate1);
    LLMChain chain2 = new LLMChain(openAIChat, promptTemplate2);
    SequentialChain sequentialChain =
        SequentialChain.builder().setChains(ImmutableList.of(chain1, chain2)).build();

    // Act.
    String result = sequentialChain.run("Motorcycle");

    // Assert.
    assertThat(result).isEqualTo(slogan);
  }
}
