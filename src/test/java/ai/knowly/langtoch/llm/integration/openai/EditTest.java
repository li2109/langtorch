package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.OpenAIHttpException;
import ai.knowly.langtoch.llm.integration.openai.service.schema.edit.EditRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.edit.EditResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class EditTest {

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void edit() throws OpenAIHttpException {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
    EditRequest request =
        EditRequest.builder()
            .model("text-davinci-edit-001")
            .input("What day of the wek is it?")
            .instruction("Fix the spelling mistakes")
            .build();

    EditResult result = service.createEdit(request);
    assertNotNull(result.getChoices().get(0).getText());
  }
}
