package ai.knowly.langtoch.llm.message;

/** A abstract class for a message. */
public abstract class BaseMessage {
  public abstract String returnMessage();

  @Override
  public String toString() {
    return String.format("Role: UNKNOWN(Base Message), Content: %s", returnMessage());
  }
}
