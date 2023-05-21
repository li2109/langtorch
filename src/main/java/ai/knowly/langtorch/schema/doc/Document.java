package ai.knowly.langtorch.schema.doc;

import ai.knowly.langtorch.schema.io.Input;

public class Document implements Input {
  private final String text;

  public Document(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
