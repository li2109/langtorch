package ai.knowly.langtoch.capability.unit.local.v1;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.junit.runners.JUnit4.class)
public class LocalCapabilityUnitReaderTest {
  private static final String CAPABILITY_PATH =
      "src/test/java/ai/knowly/langtoch/capability/unit/local/v1/summarize";

  @Test
  public void testParse() {
    // Arrange.
    LocalCapabilityUnitReader localCapabilityUnitReader =
        new LocalCapabilityUnitReader(CAPABILITY_PATH, new Gson());
    // Act.
    CapabilityConfig capabilityConfig = localCapabilityUnitReader.getConfig();
    String prompt = localCapabilityUnitReader.getPrompt();

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

    assertThat(prompt)
        .isEqualTo(
            "[SUMMARIZATION RULES]\n"
                + "DONT WASTE WORDS\n"
                + "USE SHORT, CLEAR, COMPLETE SENTENCES.\n"
                + "DO NOT USE BULLET POINTS OR DASHES.\n"
                + "USE ACTIVE VOICE.\n"
                + "MAXIMIZE DETAIL, MEANING\n"
                + "FOCUS ON THE CONTENT\n"
                + "\n"
                + "[BANNED PHRASES]\n"
                + "This article\n"
                + "This document\n"
                + "This page\n"
                + "This material\n"
                + "[END LIST]\n"
                + "\n"
                + "Summarize:\n"
                + "Hello how are you?\n"
                + "+++++\n"
                + "Hello\n"
                + "\n"
                + "Summarize this\n"
                + "{{$input}}\n"
                + "+++++");
  }
}
