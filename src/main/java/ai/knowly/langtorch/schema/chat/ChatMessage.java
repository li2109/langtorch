package ai.knowly.langtorch.schema.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage extends Message {
  private final Role role;

  @JsonCreator
  public ChatMessage(@JsonProperty("content") String content, @JsonProperty("role") Role role) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChatMessage)) return false;

    ChatMessage that = (ChatMessage) o;

    if (getRole() != that.getRole()) return false;
    return getContent() != null
        ? getContent().equals(that.getContent())
        : that.getContent() == null;
  }
}
