package ai.knowly.langtoch.llm.schema.io.input;

/** A model input that is a text string. */
public class SingleTextInput implements Input {
  private final String text;

  private SingleTextInput(String text) {
    this.text = text;
  }

  public static SingleTextInput of(String text) {
    return new SingleTextInput(text);
  }

  public String getText() {
    return text;
  }
}
