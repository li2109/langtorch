package ai.knowly.langtorch.llm.openai.schema.dto.completion.chat;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class Function implements Serializable {
  private String name;
  private String description;
  private Parameters parameters;
}
