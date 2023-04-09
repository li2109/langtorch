package ai.knowly.langtoch.llm.message;

import lombok.Builder;

/** A message from the assistant. */
@Builder(setterPrefix = "set")
public final class AssistantMessage extends BaseChatMessage {

  private final String message;

  public AssistantMessage(String message) {
    this.message = message;
  }

  @Override
  public String returnMessage() {
    return message;
  }

  @Override
  public Role returnRole() {
    return Role.ASSISTANT;
  }
}
