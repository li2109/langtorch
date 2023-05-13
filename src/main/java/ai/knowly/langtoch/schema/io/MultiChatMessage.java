package ai.knowly.langtoch.schema.io;

import ai.knowly.langtoch.schema.chat.ChatMessage;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class MultiChatMessage implements Input, Output {
  private final ImmutableList<ChatMessage> messages;

  private MultiChatMessage(Iterable<ChatMessage> messages) {
    this.messages = ImmutableList.copyOf(messages);
  }

  public static MultiChatMessage copyOf(Iterable<ChatMessage> messages) {
    return new MultiChatMessage(messages);
  }

  public static MultiChatMessage of(ChatMessage... messages) {
    return new MultiChatMessage(ImmutableList.copyOf(messages));
  }

  public static MultiChatMessage of(Iterable<ChatMessage> messages) {
    return new MultiChatMessage(ImmutableList.copyOf(messages));
  }

  public List<ChatMessage> getMessages() {
    return messages;
  }
}
