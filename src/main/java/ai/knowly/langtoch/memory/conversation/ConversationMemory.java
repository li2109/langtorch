package ai.knowly.langtoch.memory.conversation;

import ai.knowly.langtoch.memory.Memory;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map.Entry;

/** Implementation of Memory for storing conversation-related key-value pairs. */
@AutoValue
public abstract class ConversationMemory extends Memory<Role, ChatMessage> {

  public static ConversationMemory create() {
    return new AutoValue_ConversationMemory(LinkedHashMultimap.create());
  }

  abstract Multimap<Role, ChatMessage> memory();

  @Override
  public void add(Role key, ChatMessage value) {
    memory().put(key, value);
  }

  @Override
  public List<ChatMessage> get(Role key) {
    return ImmutableList.copyOf(memory().get(key));
  }

  @Override
  public void clear() {
    memory().clear();
  }

  @Override
  public Multimap<Role, ChatMessage> getMemory() {
    return memory();
  }

  @Override
  public String getMemoryContext() {
    if (memory().isEmpty()) {
      return "";
    }

    StringBuilder prompt = new StringBuilder();
    prompt.append("Current conversation:\n");
    for (Entry<Role, ChatMessage> entry : memory().entries()) {
      prompt.append(entry.getKey().toString());
      prompt.append(": ");
      prompt.append(entry.getValue().getContent());
      prompt.append("\n");
    }
    prompt.append("\n");
    return prompt.toString();
  }

  public boolean isEmpty() {
    return memory().isEmpty();
  }
}
