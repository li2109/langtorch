package ai.knowly.langtorch.preprocessing.parser;

import ai.knowly.langtorch.schema.chat.ChatMessage;

/** Implements a parser to convert a ChatMessage object to a String by returning its content. */
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
