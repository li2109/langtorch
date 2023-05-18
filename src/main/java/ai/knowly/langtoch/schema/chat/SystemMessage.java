package ai.knowly.langtoch.schema.chat;

/** A message from the system. */
public class SystemMessage extends ChatMessage {

  public SystemMessage(String content) {
    super(content, Role.SYSTEM);
  }

  public static SystemMessage of(String content) {
    return new SystemMessage(content);
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }
}
