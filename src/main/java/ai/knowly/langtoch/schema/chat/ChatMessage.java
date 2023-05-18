package ai.knowly.langtoch.schema.chat;

/** A abstract class for a chat message. */
public class ChatMessage extends Message {
  private final Role role;

  public ChatMessage(String content, Role role) {
    super(content);
    this.role = role;
  }

  public static ChatMessage of(String content, Role role) {
    return new ChatMessage(content, role);
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }
}
