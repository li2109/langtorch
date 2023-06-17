package ai.knowly.langtorch.llm.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ai.knowly.langtorch.llm.openai.schema.dto.OpenAIHttpParseException;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditResult;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class EditTest {
  @Inject private OpenAIService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new OpenAIServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
  void edit() throws OpenAIHttpParseException {
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
