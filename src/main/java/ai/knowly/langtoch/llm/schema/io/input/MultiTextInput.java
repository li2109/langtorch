package ai.knowly.langtoch.llm.schema.io.input;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class MultiTextInput implements Input {
  private final ImmutableList<String> texts;

  private MultiTextInput(String... texts) {
    this.texts = ImmutableList.copyOf(texts);
  }

  private MultiTextInput(Iterable<String> texts) {
    this.texts = ImmutableList.copyOf(texts);
  }

  public static MultiTextInput of(String... texts) {
    return new MultiTextInput(texts);
  }

  public static MultiTextInput copyOf(Iterable<String> texts) {
    return new MultiTextInput(texts);
  }

  public List<String> getTexts() {
    return texts;
  }
}
