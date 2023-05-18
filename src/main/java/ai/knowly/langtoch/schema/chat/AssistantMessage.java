package ai.knowly.langtoch.schema.chat;

/**
 * A message from the assistant.
 */
public final class AssistantMessage {

  private AssistantMessage() {
  }

  public static ChatMessage of(String content) {
    return new ChatMessage(content, Role.ASSISTANT);
  }

}
