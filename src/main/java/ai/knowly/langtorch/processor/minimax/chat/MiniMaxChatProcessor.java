package ai.knowly.langtorch.processor.minimax.chat;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import ai.knowly.langtorch.processor.Processor;
import ai.knowly.langtorch.schema.chat.*;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import javax.inject.Inject;

/**
 * MiniMax chat module implementation. Handles chat input and output for the MiniMax Language Model.
 *
 * @author maxiao
 * @date 2023/06/08
 */
public class MiniMaxChatProcessor implements Processor<MultiChatMessage, ChatMessage> {

  // MiniMaxApi instance used for making requests
  private final MiniMaxService miniMaxService;
  // Configuration for the MiniMax Chat Processor
  private MiniMaxChatProcessorConfig miniMaxChatProcessorConfig;

  @Inject
  public MiniMaxChatProcessor(
      MiniMaxService miniMaxService, MiniMaxChatProcessorConfig miniMaxChatProcessorConfig) {
    this.miniMaxService = miniMaxService;
    this.miniMaxChatProcessorConfig = miniMaxChatProcessorConfig;
  }

  // Method to run the module with the given input and return the output chat message
  @Override
  public ChatMessage run(MultiChatMessage inputData) {
    ChatCompletionRequest chatCompletionRequest =
        MiniMaxChatProcessorRequestConverter.convert(miniMaxChatProcessorConfig, inputData);
    ChatCompletionResult chatCompletion =
        miniMaxService.createChatCompletion(chatCompletionRequest);
    List<ChatCompletionResult.Choices> choices = chatCompletion.getChoices();
    ChatCompletionResult.Choices choicesResult = choices.get(0);
    return ChatMessage.of(choicesResult.getText(), Role.MINIMAX_BOT);
  }

  @Override
  public ListenableFuture<ChatMessage> runAsync(MultiChatMessage inputData) {
    ChatCompletionRequest chatCompletionRequest =
        MiniMaxChatProcessorRequestConverter.convert(miniMaxChatProcessorConfig, inputData);
    ListenableFuture<ChatCompletionResult> chatCompletionAsync =
        miniMaxService.createChatCompletionAsync(chatCompletionRequest);
    return FluentFuture.from(chatCompletionAsync)
        .transform(
            chatCompletion -> {
              miniMaxService.checkResp(chatCompletion.getBaseResp());

              List<ChatCompletionResult.Choices> choices = chatCompletion.getChoices();
              ChatCompletionResult.Choices choicesResult = choices.get(0);
              return ChatMessage.of(choicesResult.getText(), Role.MINIMAX_BOT);
            },
            directExecutor());
  }
}
