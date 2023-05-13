package ai.knowly.langtoch.capability.local.v1;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocalProcessingUnitReaderConfigTest {
  private Gson gson;

  @Before
  public void setUp() {
    gson = new Gson();
  }

  @Test
  public void testCapabilityConfig_fromJson() {
    // Arrange.
    String json =
        "{\n"
            + "  \"schema\": 1,\n"
            + "  \"type\": \"completion\",\n"
            + "  \"description\": \"Summarize given text or any text document\",\n"
            + "  \"completion\": {\n"
            + "    \"max_tokens\": 512,\n"
            + "    \"temperature\": 0.0,\n"
            + "    \"top_p\": 0.0,\n"
            + "    \"presence_penalty\": 0.0,\n"
            + "    \"frequency_penalty\": 0.0\n"
            + "  },\n"
            + "  \"input\": {\n"
            + "    \"parameters\": [\n"
            + "      {\n"
            + "        \"name\": \"input\",\n"
            + "        \"description\": \"Text to summarize\",\n"
            + "        \"defaultValue\": \"\"\n"
            + "      }\n"
            + "    ]\n"
            + "  }\n"
            + "}";

    // Act.
    CapabilityConfig capabilityConfig = gson.fromJson(json, CapabilityConfig.class);

    // Assert.
    assertThat(capabilityConfig.getSchema()).isEqualTo(1);
    assertThat(capabilityConfig.getDescription())
        .isEqualTo("Summarize given text or any text document");
    assertThat(capabilityConfig.getType()).isEqualTo("completion");

    Completion completion = capabilityConfig.getCompletion();
    assertThat(completion.getMaxTokens()).isEqualTo(512);
    assertThat(completion.getTemperature()).isEqualTo(0.0);
    assertThat(completion.getTopP()).isEqualTo(0.0);
    assertThat(completion.getPresencePenalty()).isEqualTo(0.0);
    assertThat(completion.getFrequencyPenalty()).isEqualTo(0.0);

    Input input = capabilityConfig.getInput();
    List<Parameter> parameters = input.getParameters();
    assertThat(parameters).hasSize(1);

    Parameter parameter = parameters.get(0);
    assertThat(parameter.getName()).isEqualTo("input");
    assertThat(parameter.getDescription()).isEqualTo("Text to summarize");
    assertThat(parameter.getDefaultValue()).isEqualTo("");
  }

  @Test
  public void testScientificAbstractConfig() {
    // Arrange.
    String json =
        "{\n"
            + "  \"schema\": 1,\n"
            + "  \"type\": \"completion\",\n"
            + "  \"description\": \"Given a scientific white paper abstract, rewrite it to make it"
            + " more readable\",\n"
            + "  \"completion\": {\n"
            + "    \"max_tokens\": 4000,\n"
            + "    \"temperature\": 0.0,\n"
            + "    \"top_p\": 1.0,\n"
            + "    \"presence_penalty\": 0.0,\n"
            + "    \"frequency_penalty\": 2.0\n"
            + "  }\n"
            + "}";

    // Act.
    CapabilityConfig scientificAbstractConfig = gson.fromJson(json, CapabilityConfig.class);

    // Assert.
    assertThat(scientificAbstractConfig.getSchema()).isEqualTo(1);
    assertThat(scientificAbstractConfig.getDescription())
        .isEqualTo("Given a scientific white paper abstract, rewrite it to make it more readable");
    assertThat(scientificAbstractConfig.getType()).isEqualTo("completion");

    Completion completion = scientificAbstractConfig.getCompletion();
    assertThat(completion.getMaxTokens()).isEqualTo(4000);
    assertThat(completion.getTemperature()).isWithin(0.001).of(0.0);
    assertThat(completion.getTopP()).isWithin(0.001).of(1.0);
    assertThat(completion.getPresencePenalty()).isWithin(0.001).of(0.0);
    assertThat(completion.getFrequencyPenalty()).isWithin(0.001).of(2.0);
  }

  @Test
  public void testPersonalInfoConfig() {
    // Arrange.
    String json =
        "{\n"
            + "  \"schema\": 1,\n"
            + "  \"description\": \"Ask the AI for answers contextually relevant to you based on"
            + " your name, address and pertinent information retrieved from your personal secondary"
            + " memory\",\n"
            + "  \"type\": \"completion\",\n"
            + "  \"completion\": {\n"
            + "    \"max_tokens\": 256,\n"
            + "    \"temperature\": 0.0,\n"
            + "    \"top_p\": 0.0,\n"
            + "    \"presence_penalty\": 0.0,\n"
            + "    \"frequency_penalty\": 0.0,\n"
            + "    \"stop_sequences\": [\n"
            + "      \"[done]\"\n"
            + "    ]\n"
            + "  }\n"
            + "}";

    // Act.
    CapabilityConfig personalInfoConfig = gson.fromJson(json, CapabilityConfig.class);

    // Assert.
    assertThat(personalInfoConfig.getSchema()).isEqualTo(1);
    assertThat(personalInfoConfig.getDescription())
        .isEqualTo(
            "Ask the AI for answers contextually relevant to you based on your name, address and"
                + " pertinent information retrieved from your personal secondary memory");
    assertThat(personalInfoConfig.getType()).isEqualTo("completion");

    Completion completion = personalInfoConfig.getCompletion();
    assertThat(completion.getMaxTokens()).isEqualTo(256);
    assertThat(completion.getTemperature()).isWithin(0.001).of(0.0);
    assertThat(completion.getTopP()).isWithin(0.001).of(0.0);
    assertThat(completion.getPresencePenalty()).isWithin(0.001).of(0.0);
    assertThat(completion.getFrequencyPenalty()).isWithin(0.001).of(0.0);
    assertThat(completion.getStopSequences()).containsExactly("[done]");
  }
}
