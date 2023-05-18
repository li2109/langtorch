package ai.knowly.langtoch.parser;

import ai.knowly.langtoch.schema.chat.ChatMessage;

public class ChatMessageToStringParser implements Parser<ChatMessage, String> {

  private ChatMessageToStringParser() {
    super();
  }

  public static ChatMessageToStringParser create() {
    return new ChatMessageToStringParser();
  }

  @Override
  public String parse(ChatMessage chatMessage) {
    return chatMessage.getContent();
  }
}
