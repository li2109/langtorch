package ai.knowly.langtoch.llm.schema.chat;

import ai.knowly.langtoch.llm.schema.io.input.Input;
import ai.knowly.langtoch.llm.schema.io.output.Output;

/** A abstract class for a chat message. */
public abstract class ChatMessage extends Message implements Input, Output {
  public static ChatMessage of(Role role, String message) {
    return new ChatMessage() {
      @Override
      public Role getRole() {
        return role;
      }

      @Override
      public String getMessage() {
        return message;
      }
    };
  }

  public abstract Role getRole();

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getMessage());
  }
}
