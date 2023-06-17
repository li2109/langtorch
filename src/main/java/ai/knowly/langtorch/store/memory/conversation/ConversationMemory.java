package ai.knowly.langtorch.store.memory.conversation;

import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.store.memory.Memory;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/** Implementation of Memory for storing conversation-related key-value pairs. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class ConversationMemory implements Memory<ChatMessage, ConversationMemoryContext> {
  @Builder.Default private List<ChatMessage> chatMessages = new ArrayList<>();

  public static ConversationMemory geDefaultInstance() {
    return ConversationMemory.builder().build();
  }

  @Override
  public void add(ChatMessage value) {
    chatMessages.add(value);
  }

  @Override
  public List<ChatMessage> getAll() {
    return chatMessages;
  }

  @Override
  public void clear() {
    chatMessages.clear();
  }

  @Override
  public ConversationMemoryContext getMemoryContext() {
    return ConversationMemoryContext.builder().setChatMessages(chatMessages).build();
  }
}
