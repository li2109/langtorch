package ai.knowly.langtorch.preprocessing.parser;

import static ai.knowly.langtorch.schema.chat.Role.USER;

import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;

public final class StringToMultiChatMessageParser implements Parser<String, MultiChatMessage> {

  private StringToMultiChatMessageParser() {
    super();
  }

  public static StringToMultiChatMessageParser create() {
    return new StringToMultiChatMessageParser();
  }

  @Override
  public MultiChatMessage parse(String content) {
    return MultiChatMessage.of(ChatMessage.of(content, USER));
  }
}
