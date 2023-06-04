package ai.knowly.langtorch.llm.openai;

import static ai.knowly.langtorch.util.OpenAIServiceTestingUtils.OPENAI_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ai.knowly.langtorch.llm.openai.schema.dto.OpenAIHttpParseException;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class EditTest {

  @Test
  void edit() throws OpenAIHttpParseException {
    OpenAIService service = OPENAI_TESTING_SERVICE;
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
