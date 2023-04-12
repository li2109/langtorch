package ai.knowly.langtoch.llm.message;

import com.google.auto.value.AutoValue;

/** A message from the assistant. */
@AutoValue
public abstract class AssistantMessage extends BaseChatMessage {
  public static Builder builder() {
    return new AutoValue_AssistantMessage.Builder();
  }

  @Override
  public abstract String getMessage();

  @Override
  public Role getRole() {
    return Role.ASSISTANT;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setMessage(String message);

    public abstract AssistantMessage build();
  }
}
