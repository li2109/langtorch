package ai.knowly.langtorch.llm.huggingface.schema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * CreateTextGenerationTaskRequest is a DTO class for the request body of the Text Generation API.
 */
@AllArgsConstructor
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class CreateTextGenerationTaskRequest {
  @NonNull private String inputs;
  private Parameters parameters;
  private Options options;
}
