package ai.knowly.langtorch.schema.chat;

/** A message from the system. */
public final class SystemMessage {

  private SystemMessage() {}

  public static ChatMessage of(String content) {
    return new ChatMessage(content, Role.SYSTEM, "");
  }
}
