package ai.knowly.langtoch.llm.message;

import lombok.Builder;

/** A message from the system. */
@Builder(setterPrefix = "set")
public final class SystemMessage extends BaseChatMessage {

  private final String message;

  public SystemMessage(String message) {
    this.message = message;
  }

  @Override
  public String returnMessage() {
    return message;
  }

  @Override
  public Role returnRole() {
    return Role.SYSTEM;
  }
}
