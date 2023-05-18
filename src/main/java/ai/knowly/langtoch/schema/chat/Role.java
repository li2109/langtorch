package ai.knowly.langtoch.schema.chat;

/** A enum for the role of a message. */
public enum Role {
  SYSTEM("system"),
  USER("user"),
  ASSISTANT("assistant");

  private final String value;

  Role(final String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
