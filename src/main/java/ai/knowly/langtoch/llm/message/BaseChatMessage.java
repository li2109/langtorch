package ai.knowly.langtoch.llm.message;

/** A abstract class for a chat message. */
public abstract class BaseChatMessage extends BaseMessage {
  public abstract Role getRole();

  @Override
  public String toString() {
    return String.format("%s: %s", getRole(), getMessage());
  }
}
