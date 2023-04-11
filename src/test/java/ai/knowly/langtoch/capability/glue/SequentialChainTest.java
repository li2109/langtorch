//package ai.knowly.langtoch.capability.glue;
//
//import static com.google.common.truth.Truth.assertThat;
//import static org.mockito.Mockito.when;
//
//import ai.knowly.langtoch.capability.glue.SequentialChain;
//import ai.knowly.langtoch.capability.unit.SimpleLLMCapabilityUnit;
//import ai.knowly.langtoch.llm.providers.openai.OpenAIChat;
//import ai.knowly.langtoch.prompt.PromptTemplate;
//import com.google.common.collect.ImmutableList;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SequentialChainTest {
//  @Mock OpenAIChat openAIChat;
//
//  @Test
//  public void testSequentialMChain() {
//    // Arrange.
//    String prompt1 = "Write a creative company name if the product is Motorcycle.";
//    String companyName = "Awesome Motorcycle";
//    String prompt2 = "Write a slogan for a Awesome Motorcycle company.";
//    String slogan = "Awesome! Motorcycle!";
//
//    when(openAIChat.run(prompt1)).thenReturn(companyName);
//    when(openAIChat.run(prompt2)).thenReturn(slogan);
//
//    PromptTemplate promptTemplate1 =
//        PromptTemplate.builder()
//            .setTemplate("Write a creative company name if the product is {{$product}}.")
//            .build();
//    PromptTemplate promptTemplate2 =
//        PromptTemplate.builder()
//            .setTemplate("Write a slogan for a {{$company_name}} company.")
//            .build();
//
//    SimpleLLMCapabilityUnit chain1 = new SimpleLLMCapabilityUnit(openAIChat, promptTemplate1);
//    SimpleLLMCapabilityUnit chain2 = new SimpleLLMCapabilityUnit(openAIChat, promptTemplate2);
//    SequentialChain sequentialChain =
//        SequentialChain.builder().setChains(ImmutableList.of(chain1, chain2)).build();
//
//    // Act.
//    String result = sequentialChain.run("Motorcycle");
//
//    // Assert.
//    assertThat(result).isEqualTo(slogan);
//  }
//}
