package ai.knowly.langtoch.memory.conversation;

import ai.knowly.langtoch.memory.Memory;
import java.util.LinkedHashMap;
import java.util.Optional;

/** Implementation of Memory for storing conversation-related key-value pairs. */
public class ConversationMemory extends Memory<String, String> {
  // LinkedHashMap to store the conversation-related key-value pairs and make sure that the order of
  // insertion is preserved.
  private final LinkedHashMap<String, String> memory = new LinkedHashMap<>();

  /**
   * Adds a key-value pair to the conversation memory.
   *
   * @param key the key to add
   * @param value the value to associate with the key
   */
  @Override
  public void add(String key, String value) {
    memory.put(key, value);
  }

  /**
   * Retrieves the value associated with the specified key, if present, in the conversation memory.
   *
   * @param key the key whose associated value is to be returned
   * @return an Optional containing the value associated with the key, or an empty Optional if the
   *     key is not present
   */
  @Override
  public Optional<String> get(String key) {
    return memory.containsKey(key) ? Optional.of(memory.get(key)) : Optional.empty();
  }

  /**
   * Removes the key-value pair associated with the specified key from the conversation memory.
   *
   * @param key the key whose associated key-value pair is to be removed
   */
  @Override
  public void remove(String key) {
    memory.remove(key);
  }

  /** Removes all key-value pairs from the conversation memory. */
  @Override
  public void clear() {
    memory.clear();
  }

  /**
   * Returns the conversation memory.
   *
   * @return the conversation memory
   */
  @Override
  public LinkedHashMap<String, String> getMemory() {
    return memory;
  }
}
