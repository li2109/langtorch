package ai.knowly.langtoch.schema.chat;

/** A abstract class for a message. */
public abstract class Message {
  public abstract String getMessage();

  @Override
  public String toString() {
    return String.format("Role: UNKNOWN(Base Message), Content: %s", getMessage());
  }
}
