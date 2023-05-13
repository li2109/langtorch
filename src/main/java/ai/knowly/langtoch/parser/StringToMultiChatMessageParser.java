package ai.knowly.langtoch.parser;

import static ai.knowly.langtoch.schema.chat.Role.USER;

import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.io.MultiChatMessage;

public final class StringToMultiChatMessageParser implements Parser<String, MultiChatMessage> {

  private StringToMultiChatMessageParser() {
    super();
  }

  public static StringToMultiChatMessageParser create() {
    return new StringToMultiChatMessageParser();
  }

  @Override
  public MultiChatMessage parse(String input) {
    return MultiChatMessage.of(ChatMessage.of(USER, input));
  }
}
