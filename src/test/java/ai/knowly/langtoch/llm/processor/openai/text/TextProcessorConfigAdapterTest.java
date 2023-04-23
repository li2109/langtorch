package ai.knowly.langtoch.llm.processor.openai.text;

import static com.google.common.truth.Truth.assertThat;

import com.theokanning.openai.completion.CompletionRequest;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TextProcessorConfigAdapterTest {

  private TextProcessorConfig textProcessorConfig;

  @Before
  public void setUp() {
    textProcessorConfig =
        TextProcessorConfig.builder()
            .setModel("text-davinci-003")
            .setSuffix("Test suffix")
            .setMaxTokens(100)
            .setTemperature(0.8)
            .setTopP(0.9)
            .setN(5)
            .setStream(true)
            .setLogprobs(10)
            .setEcho(true)
            .setStop(List.of("stop1", "stop2"))
            .setPresencePenalty(0.5)
            .setFrequencyPenalty(0.6)
            .setBestOf(3)
            .setLogitBias(Map.of("key1", 50, "key2", -20))
            .setUser("user123")
            .build();
  }

  @Test
  public void testConvert() {
    CompletionRequest completionRequest =
        TextProcessorRequestConverter.convert(textProcessorConfig, "Test prompt");

    assertThat(completionRequest.getModel()).isEqualTo(textProcessorConfig.getModel());
    assertThat(completionRequest.getPrompt()).isEqualTo("Test prompt");
    assertThat(completionRequest.getSuffix()).isEqualTo(textProcessorConfig.getSuffix().get());
    assertThat(completionRequest.getMaxTokens())
        .isEqualTo(textProcessorConfig.getMaxTokens().get());
    assertThat(completionRequest.getTemperature())
        .isEqualTo(textProcessorConfig.getTemperature().get());
    assertThat(completionRequest.getTopP()).isEqualTo(textProcessorConfig.getTopP().get());
    assertThat(completionRequest.getN()).isEqualTo(textProcessorConfig.getN().get());
    assertThat(completionRequest.getStream()).isEqualTo(textProcessorConfig.getStream().get());
    assertThat(completionRequest.getLogprobs()).isEqualTo(textProcessorConfig.getLogprobs().get());
    assertThat(completionRequest.getEcho()).isEqualTo(textProcessorConfig.getEcho().get());
    assertThat(completionRequest.getStop()).isEqualTo(textProcessorConfig.getStop());
    assertThat(completionRequest.getPresencePenalty())
        .isEqualTo(textProcessorConfig.getPresencePenalty().get());
    assertThat(completionRequest.getFrequencyPenalty())
        .isEqualTo(textProcessorConfig.getFrequencyPenalty().get());
    assertThat(completionRequest.getBestOf()).isEqualTo(textProcessorConfig.getBestOf().get());
    assertThat(completionRequest.getLogitBias()).isEqualTo(textProcessorConfig.getLogitBias());
    assertThat(completionRequest.getUser()).isEqualTo(textProcessorConfig.getUser().get());
  }
}
