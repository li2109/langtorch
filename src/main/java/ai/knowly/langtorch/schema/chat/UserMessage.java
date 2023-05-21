package ai.knowly.langtorch.schema.chat;

/** A message from the user. */
public class UserMessage extends ChatMessage {
  private final String content;

  public UserMessage(String content) {
    super(content, Role.USER);
    this.content = content;
  }

  public static UserMessage of(String content) {
    return new UserMessage(content);
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getContent());
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof UserMessage && ((UserMessage) obj).getContent().equals(getContent()))
        || (obj instanceof ChatMessage
            && ((ChatMessage) obj).getContent().equals(getContent())
            && ((ChatMessage) obj).getRole().equals(Role.USER));
  }

  @Override
  public int hashCode() {
    return getContent().hashCode();
  }
}
