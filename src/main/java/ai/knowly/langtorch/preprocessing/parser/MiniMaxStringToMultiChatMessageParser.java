package ai.knowly.langtorch.preprocessing.parser;

import ai.knowly.langtorch.schema.chat.MiniMaxUserMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;

/**
 * @author maxiao
 * @date 2023/06/14
 */
public final class MiniMaxStringToMultiChatMessageParser
    implements Parser<String, MultiChatMessage> {

  private MiniMaxStringToMultiChatMessageParser() {
    super();
  }

  public static MiniMaxStringToMultiChatMessageParser create() {
    return new MiniMaxStringToMultiChatMessageParser();
  }

  @Override
  public MultiChatMessage parse(String content) {
    return MultiChatMessage.of(MiniMaxUserMessage.of(content));
  }
}
