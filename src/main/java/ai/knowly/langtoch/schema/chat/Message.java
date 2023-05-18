package ai.knowly.langtoch.schema.chat;

import ai.knowly.langtoch.schema.io.Input;
import ai.knowly.langtoch.schema.io.Output;

/** A abstract class for a message. */
// public abstract class Message {
//  public abstract String getMessage();
//
//  @Override
//  public String toString() {
//    return String.format("Role: UNKNOWN(Base Message), Content: %s", getMessage());
//  }
// }
public class Message implements Input, Output {
  private final String content;

  public Message(String content) {
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
