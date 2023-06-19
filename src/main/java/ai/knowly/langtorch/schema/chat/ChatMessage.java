package ai.knowly.langtorch.schema.chat;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.FunctionCall;
import ai.knowly.langtorch.store.memory.MemoryValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage extends Message implements MemoryValue {
  private final Role role;
  private String name;
  private FunctionCall functionCall;

  @JsonCreator
  public ChatMessage(
      @JsonProperty("content") String content,
      @JsonProperty("role") Role role,
      @JsonProperty("name") String name,
      @JsonProperty("function_call") FunctionCall functionCall) {
    super(content);
    this.role = role;
    this.name = name;
    this.functionCall = functionCall;
  }

  public Role getRole() {
    return role;
  }

  public String getName() {
    return name;
  }

  public FunctionCall getFunctionCall() {
    return functionCall;
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
