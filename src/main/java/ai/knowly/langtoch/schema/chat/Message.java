package ai.knowly.langtoch.schema.chat;

import ai.knowly.langtoch.schema.io.Input;
import ai.knowly.langtoch.schema.io.Output;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message implements Input, Output {
  private final String content;

  @JsonCreator
  public Message(@JsonProperty("content") String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.format("Role: UNKNOWN(Base Message), Content: %s", getContent());
  }
}
