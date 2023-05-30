package ai.knowly.langtorch.preprocessing.parser;

import ai.knowly.langtorch.schema.chat.ChatMessage;

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
