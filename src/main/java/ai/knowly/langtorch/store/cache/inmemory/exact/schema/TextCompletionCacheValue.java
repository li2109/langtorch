package ai.knowly.langtorch.store.cache.inmemory.exact.schema;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TextCompletionCacheValue {
  private List<String> completion;
}
