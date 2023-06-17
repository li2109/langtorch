package ai.knowly.langtorch.llm.openai.schema.dto.completion.chat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class Parameters implements Serializable {
  private String type;
  private Map<String, Object> properties;
  private List<String> required;
}
