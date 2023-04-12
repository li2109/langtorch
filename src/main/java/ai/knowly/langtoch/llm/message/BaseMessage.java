package ai.knowly.langtoch.llm.message;

/** A abstract class for a message. */
public abstract class BaseMessage {
  public abstract String getMessage();

  @Override
  public String toString() {
    return String.format("Role: UNKNOWN(Base Message), Content: %s", getMessage());
  }
}
