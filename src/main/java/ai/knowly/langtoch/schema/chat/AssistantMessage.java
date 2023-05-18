package ai.knowly.langtoch.schema.chat;

public class AssistantMessage extends ChatMessage {

  public AssistantMessage(String content) {
    super(content, Role.ASSISTANT);
  }

  public static AssistantMessage of(String content) {
    return new AssistantMessage(content);
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }
}
