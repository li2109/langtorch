package ai.knowly.langtorch.schema.text;

import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;

/** A model input/output that is a text string. */
public class SingleText implements Input, Output {
  private final String text;

  private SingleText(String text) {
    this.text = text;
  }

  public static SingleText of(String text) {
    return new SingleText(text);
  }

  public String getText() {
    return text;
  }
}
