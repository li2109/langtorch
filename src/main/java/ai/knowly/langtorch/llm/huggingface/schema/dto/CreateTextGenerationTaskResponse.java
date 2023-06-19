package ai.knowly.langtorch.llm.huggingface.schema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateTextGenerationTaskResponse is a DTO class for the response body of the Text Generation API.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateTextGenerationTaskResponse {
  @JsonProperty("generated_text")
  private String generatedText;
}
