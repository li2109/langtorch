package ai.knowly.langtoch.memory.conversation;

import ai.knowly.langtoch.llm.message.BaseChatMessage;
import ai.knowly.langtoch.llm.message.Role;
import ai.knowly.langtoch.memory.Memory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map.Entry;

/** Implementation of Memory for storing conversation-related key-value pairs. */
public class ConversationMemory extends Memory<Role, BaseChatMessage> {
  // LinkedHashMap to store the conversation-related key-value pairs and make sure that the order of
  // insertion is preserved.
  private final Multimap<Role, BaseChatMessage> memory = LinkedHashMultimap.create();

  @Override
  public void add(Role key, BaseChatMessage value) {
    memory.put(key, value);
  }

  @Override
  public List<BaseChatMessage> get(Role key) {
    return ImmutableList.copyOf(memory.get(key));
  }

  @Override
  public void clear() {
    memory.clear();
  }

  @Override
  public Multimap<Role, BaseChatMessage> getMemory() {
    return memory;
  }

  @Override
  public String getMemoryContext() {
    if (memory.isEmpty()) {
      return "";
    }

    StringBuilder prompt = new StringBuilder();
    prompt.append("Current conversation:\n");
    for (Entry<Role, BaseChatMessage> entry : memory.entries()) {
      prompt.append(entry.getKey().toString());
      prompt.append(": ");
      prompt.append(entry.getValue().getMessage());
      prompt.append("\n");
    }
    prompt.append("\n");
    return prompt.toString();
  }

  public boolean isEmpty() {
    return memory.isEmpty();
  }
}
