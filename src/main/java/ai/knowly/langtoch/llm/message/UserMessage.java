package ai.knowly.langtoch.llm.message;

import com.google.auto.value.AutoValue;

/** A message from the user. */
@AutoValue
public abstract class UserMessage extends BaseChatMessage {
  public static Builder builder() {
    return new AutoValue_UserMessage.Builder();
  }

  @Override
  public abstract String getMessage();

  @Override
  public Role getRole() {
    return Role.USER;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setMessage(String message);

    public abstract UserMessage build();
  }
}
