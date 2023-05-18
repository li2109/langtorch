package ai.knowly.langtoch.schema.chat;

public class AssistantMessage extends ChatMessage {
  private final String content;

  public AssistantMessage(String content) {
    super(content, Role.ASSISTANT);
    this.content = content;
  }

  public static AssistantMessage of(String content) {
    return new AssistantMessage(content);
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }
}
