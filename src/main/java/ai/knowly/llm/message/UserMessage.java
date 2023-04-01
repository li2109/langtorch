package ai.knowly.llm.message;

import lombok.Builder;

/** A message from the user. */
@Builder(setterPrefix = "set")
public final class UserMessage extends BaseChatMessage {

  private final String message;

  public UserMessage(String message) {
    this.message = message;
  }

  @Override
  public String returnMessage() {
    return message;
  }

  @Override
  public Role returnRole() {
    return Role.USER;
  }
}
