package ai.knowly.langtorch.schema.text;

import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import ai.knowly.langtorch.store.memory.MemoryValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MultiChatMessage implements Input, Output, MemoryValue {
  private final ImmutableList<ChatMessage> messages;

  private MultiChatMessage(Iterable<ChatMessage> messages) {
    this.messages = ImmutableList.copyOf(messages);
  }

  public static Collector<ChatMessage, ?, MultiChatMessage> toMultiChatMessage() {
    return Collectors.collectingAndThen(
        Collectors.toList(), list -> new MultiChatMessage(ImmutableList.copyOf(list)));
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

  @Override
  public String toString() {
    return "MultiChatMessage{" + "messages=" + messages + '}';
  }
}
