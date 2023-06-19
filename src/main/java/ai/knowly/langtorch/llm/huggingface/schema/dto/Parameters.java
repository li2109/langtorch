package ai.knowly.langtorch.llm.huggingface.schema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, setterPrefix = "set")
public class Parameters {
  @JsonProperty("top_k")
  private Integer topK;

  @JsonProperty("top_p")
  private Float topP;

  private Float temperature;

  @JsonProperty("repetition_penalty")
  private Float repetitionPenalty;

  @JsonProperty("max_new_tokens")
  private Integer maxNewTokens;

  @JsonProperty("max_time")
  private Float maxTime;

  @JsonProperty("return_full_text")
  private Boolean returnFullText;

  @JsonProperty("num_return_sequences")
  private Integer numReturnSequences;

  @JsonProperty("do_sample")
  private Boolean doSample;
}
