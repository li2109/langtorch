package ai.knowly.langtorch.util;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import com.google.common.collect.ImmutableList;

/**
 * @author maxiao
 * @date 2023/06/07
 */
public class MiniMaxServiceTestingUtils {

  public static class ChatCompletion {
    public static ChatCompletionResult createChatCompletionResult(
        ChatCompletionRequest.Message message) {

      ImmutableList<ChatCompletionRequest.Message> chatMessages = ImmutableList.of(message);

      ChatCompletionResult completionResult = new ChatCompletionResult();
      ImmutableList.Builder<ChatCompletionResult.Choices> builder = ImmutableList.builder();
      for (ChatCompletionRequest.Message chatMessage : chatMessages) {
        ChatCompletionResult.Choices completionChoice = new ChatCompletionResult.Choices();
        completionChoice.setText(chatMessage.getText());
        builder.add(completionChoice);
      }
      ImmutableList<ChatCompletionResult.Choices> choices = builder.build();
      completionResult.setChoices(choices);
      return completionResult;
    }
  }
}
