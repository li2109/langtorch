package ai.knowly.langtoch.llm.schema.io.output;

/** A model output that is a text string. */
public class SingleTextOutput implements Output {
  private final String text;

  private SingleTextOutput(String text) {
    this.text = text;
  }

  public static SingleTextOutput of(String text) {
    return new SingleTextOutput(text);
  }

  public String getText() {
    return text;
  }
}
