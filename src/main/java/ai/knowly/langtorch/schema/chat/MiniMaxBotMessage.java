package ai.knowly.langtorch.schema.chat;

/**
 * @author maxiao
 * @date 2023/06/13
 */
public class MiniMaxBotMessage {
  private MiniMaxBotMessage() {}

  public static ChatMessage of(String content) {
    return new ChatMessage(content, Role.MINIMAX_BOT, null);
  }
}
