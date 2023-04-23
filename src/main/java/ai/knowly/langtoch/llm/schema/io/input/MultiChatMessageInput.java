package ai.knowly.langtoch.llm.schema.io.input;

import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class MultiChatMessageInput implements Input {
  private final ImmutableList<ChatMessage> messages;

  private MultiChatMessageInput(Iterable<ChatMessage> messages) {
    this.messages = ImmutableList.copyOf(messages);
  }

  public static MultiChatMessageInput copyOf(Iterable<ChatMessage> messages) {
    return new MultiChatMessageInput(messages);
  }

  public static MultiChatMessageInput of(ChatMessage... messages) {
    return new MultiChatMessageInput(ImmutableList.copyOf(messages));
  }

  public static MultiChatMessageInput of(Iterable<ChatMessage> messages) {
    return new MultiChatMessageInput(ImmutableList.copyOf(messages));
  }

  public List<ChatMessage> getMessages() {
    return messages;
  }
}
