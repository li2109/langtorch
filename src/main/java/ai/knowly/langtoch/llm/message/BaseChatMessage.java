package ai.knowly.langtoch.llm.message;

/** A abstract class for a chat message. */
public abstract class BaseChatMessage extends BaseMessage {
  public abstract String returnMessage();

  public abstract Role returnRole();

  @Override
  public String toString() {
    return String.format("Role: %s, Content: %s", returnRole(), returnMessage());
  }
}
