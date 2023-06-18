package ai.knowly.langtorch.llm.openai.schema.dto.completion.chat;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
public class Parameters {
  private String type;
  private Map<String, Object> properties;
  private List<String> required;
}
