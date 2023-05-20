package ai.knowly.langtorch.schema.chat;

/** A message from the system. */
public class SystemMessage extends ChatMessage {
  private final String content;

  public SystemMessage(String content) {
    super(content, Role.SYSTEM);
    this.content = content;
  }

  public static SystemMessage of(String content) {
    return new SystemMessage(content);
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }
}
