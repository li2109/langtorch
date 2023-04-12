package ai.knowly.langtoch.capability.glue;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.capability.unit.BaseCapabilityUnit;
import ai.knowly.langtoch.capability.unit.PromptTemplateToPromptTemplateLLMUnit;
import ai.knowly.langtoch.capability.unit.PromptTemplateToStringLLMUnit;
import ai.knowly.langtoch.llm.providers.openai.OpenAIChat;
import ai.knowly.langtoch.prompt.PromptTemplate;
import java.util.Map;
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
    String companyName = "Honda";
    String prompt2 = "Write a slogan for company(name: Honda).";
    String slogan = "The Power of Dreams!";
    when(openAIChat.run(prompt1)).thenReturn(companyName);
    when(openAIChat.run(prompt2)).thenReturn(slogan);

    String template1 = "Write a creative company name if the product is {{$product}}.";
    String template2 = "Write a slogan for company(name: {{$company_name}}).";

    // First node: Write a creative company name if the product is {{$product}}.
    // Prompt Template -> Input Parser -> String
    // -> 1st Capability Unit
    // -> String -> OutputParser -> Prompt Template

    // Second node: Write a slogan for company(name: {{$company_name}}.
    // Prompt Template(output of first node) -> Input Parser -> String
    // -> 2nd Capability Unit
    // -> String -> OutputParser -> String

    // Output parser of the first unit that takes company name generated from LLM and form the input
    // Prompt template to the second unit.
    // Process: Honda => Write a slogan for company(name: Honda).
    PromptTemplateToPromptTemplateLLMUnit promptTemplateToStringLLMUnit1 =
        new PromptTemplateToPromptTemplateLLMUnit(openAIChat, Map.of("template", template2));

    SequentialCapabilityBuilder<PromptTemplate, PromptTemplate> sequentialCapabilityBuilder =
        new SequentialCapabilityBuilder<>(promptTemplateToStringLLMUnit1);

    PromptTemplateToStringLLMUnit promptTemplateToStringLLMUnit2 =
        new PromptTemplateToStringLLMUnit(openAIChat);

    BaseCapabilityGlue<PromptTemplate, String> glue =
        sequentialCapabilityBuilder.addUnit(promptTemplateToStringLLMUnit2).build();

    // Act.
    String result =
        glue.run(
            PromptTemplate.builder()
                .setTemplate(template1)
                .addVariableValuePair("product", "Motorcycle")
                .build());

    // Assert.
    assertThat(result).isEqualTo(slogan);
  }

  @Test
  public void testWithGenericCapabilityUnit() {
    BaseCapabilityUnit<String, Integer> unit1 =
        new BaseCapabilityUnit<String, Integer>() {
          @Override
          public Integer run(String input) {
            return 1;
          }
        };

    BaseCapabilityUnit<Integer, Boolean> unit2 =
        new BaseCapabilityUnit<Integer, Boolean>() {
          @Override
          public Boolean run(Integer input) {
            return true;
          }
        };

    BaseCapabilityUnit<Boolean, String> unit3 =
        new BaseCapabilityUnit<Boolean, String>() {
          @Override
          public String run(Boolean input) {
            return "true";
          }
        };

    SequentialCapabilityBuilder<String, Integer> builder = new SequentialCapabilityBuilder<>(unit1);
    BaseCapabilityGlue<String, String> glue = builder.addUnit(unit2).addUnit(unit3).build();

    String input = "example";
    String output = glue.run(input);
    System.out.println(output); // Output: "true"
  }
}
