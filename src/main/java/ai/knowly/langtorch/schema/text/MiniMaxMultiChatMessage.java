package ai.knowly.langtorch.schema.text;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import ai.knowly.langtorch.store.memory.MemoryValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author maxiao
 * @date 2023/06/11
 */
public class MiniMaxMultiChatMessage implements Input, Output, MemoryValue {

  private final ImmutableList<ChatCompletionRequest.Message> messages;

  private MiniMaxMultiChatMessage(Iterable<ChatCompletionRequest.Message> messages) {
    this.messages = ImmutableList.copyOf(messages);
  }

  public static Collector<ChatCompletionRequest.Message, ?, MiniMaxMultiChatMessage>
      toMultiChatMessage() {
    return Collectors.collectingAndThen(
        Collectors.toList(), list -> new MiniMaxMultiChatMessage(ImmutableList.copyOf(list)));
  }

  public static MiniMaxMultiChatMessage of(ChatCompletionRequest.Message... messages) {
    return new MiniMaxMultiChatMessage(ImmutableList.copyOf(messages));
  }

  public static MiniMaxMultiChatMessage of(Iterable<ChatCompletionRequest.Message> messages) {
    return new MiniMaxMultiChatMessage(ImmutableList.copyOf(messages));
  }

  public List<ChatCompletionRequest.Message> getMessages() {
    return messages;
  }

  @Override
  public String toString() {
    return "MiniMaxMultiChatMessage{" + "messages=" + messages + '}';
  }
}
