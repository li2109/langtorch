package ai.knowly.langtoch.llm.schema.chat;

import com.google.auto.value.AutoValue;

/** A message from the user. */
@AutoValue
public abstract class UserMessage extends ChatMessage {
  public static Builder builder() {
    return new AutoValue_UserMessage.Builder();
  }

  public static UserMessage of(String message) {
    return builder().setMessage(message).build();
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
