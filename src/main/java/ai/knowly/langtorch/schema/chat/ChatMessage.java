package ai.knowly.langtorch.schema.chat;

import ai.knowly.langtorch.store.memory.MemoryValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage extends Message implements MemoryValue {
  private final Role role;
  private String name;

  @JsonCreator
  public ChatMessage(
      @JsonProperty("content") String content,
      @JsonProperty("role") Role role,
      @JsonProperty("name") String name) {
    super(content);
    this.role = role;
    this.name = name;
  }

  public static ChatMessage of(String content, Role role) {
    return new ChatMessage(content, role, null);
  }

  public static ChatMessage of(String content, Role role, String name) {
    return new ChatMessage(content, role, name);
  }

  public Role getRole() {
    return role;
  }

  public String getName() {
    return name;
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
