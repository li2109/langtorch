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
public class Options {
  @JsonProperty("use_cache")
  private Boolean useCache;

  @JsonProperty("wait_for_model")
  private Boolean waitForModel;
}
