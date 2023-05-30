package ai.knowly.langtorch.store.memory.conversation;

import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.store.memory.MemoryContext;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/** Implementation of MemoryContext for storing chat messages inside one conversation. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class ConversationMemoryContext implements MemoryContext {
  private static final String DEFAULT_CONTEXT_HEADER = "Previous conversation:\n";
  private static final String DEFAULT_FORMAT_FOR_EACH_MESSAGE = "%s: %s";

  private final List<ChatMessage> chatMessages;
  @Builder.Default private String contextHeader = DEFAULT_CONTEXT_HEADER;
  @Builder.Default private String formatForEachMessage = DEFAULT_FORMAT_FOR_EACH_MESSAGE;

  @Override
  public String get() {
    if (chatMessages.isEmpty()) {
      return "";
    }
    StringBuilder context = new StringBuilder();
    context.append(contextHeader).append("\n");
    chatMessages.forEach(
        chatMessage ->
            context
                .append(
                    String.format(
                        formatForEachMessage, chatMessage.getRole(), chatMessage.getContent()))
                .append("\n"));
    return context.toString();
  }
}
