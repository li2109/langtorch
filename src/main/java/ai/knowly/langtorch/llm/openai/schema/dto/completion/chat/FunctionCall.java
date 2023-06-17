package ai.knowly.langtorch.llm.openai.schema.dto.completion.chat;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FunctionCall implements Serializable {
  private String name;
  private String arguments;
}
