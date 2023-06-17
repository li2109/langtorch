package ai.knowly.langtorch.schema.chat;

/** A message from the user. */
public final class UserMessage {

  private UserMessage() {}

  public static ChatMessage of(String content) {
    return new ChatMessage(content, Role.USER, null);
  }
}
