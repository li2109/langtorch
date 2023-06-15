package ai.knowly.langtorch.processor.module.minimax.chat;
/**
 * @author maxiao
 * @date 2023/06/08
 */
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.processor.module.minimax.MiniMaxServiceProvider;
import ai.knowly.langtorch.schema.chat.*;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import javax.inject.Inject;

/**
 * MiniMax chat module implementation. Handles chat input and output for the MiniMax Language Model.
 */
public class MiniMaxChatProcessor implements Processor<MultiChatMessage, ChatMessage> {

  // default model
  private static final String DEFAULT_MODEL = "abab5-chat";
  // MiniMaxApi instance used for making requests
  private final MiniMaxService miniMaxService;
  // Configuration for the MiniMax Chat Processor
  private MiniMaxChatProcessorConfig miniMaxChatProcessorConfig =
      MiniMaxChatProcessorConfig.builder().setModel(DEFAULT_MODEL).build();

  // Constructor with dependency injection
  @Inject
  MiniMaxChatProcessor(MiniMaxService miniMaxService) {
    this.miniMaxService = miniMaxService;
  }

  // Private constructor used in factory methods
  private MiniMaxChatProcessor() {
    this.miniMaxService = MiniMaxServiceProvider.createMiniMaxService();
  }

  public static MiniMaxChatProcessor create() {
    return new MiniMaxChatProcessor();
  }

  public static MiniMaxChatProcessor create(String groupId, String apiKey) {
    return new MiniMaxChatProcessor(MiniMaxServiceProvider.createMiniMaxService(groupId, apiKey));
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
              List<ChatCompletionResult.Choices> choices = chatCompletion.getChoices();
              ChatCompletionResult.Choices choicesResult = choices.get(0);
              return ChatMessage.of(choicesResult.getText(), Role.MINIMAX_BOT);
            },
            directExecutor());
  }
}
