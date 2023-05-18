package ai.knowly.langtoch.llm.integration.openai.service.schema;

import java.util.List;
import lombok.Data;

/** A wrapper class to fit the OpenAI engine and search endpoints */
@Data
public class OpenAIResponse<T> {
  /** A list containing the actual results */
  public List<T> data;

  /** The type of object returned, should be "list" */
  public String object;
}
