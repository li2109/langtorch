package ai.knowly.langtorch.processor.module.minimax.chat;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author maxiao
 * @date 2023/06/08
 */
public class MiniMaxChatProcessorRequestConverter {

  private MiniMaxChatProcessorRequestConverter() {}

  public static ChatCompletionRequest convert(
      MiniMaxChatProcessorConfig miniMaxChatProcessorConfig, MultiChatMessage messages) {

    List<ChatCompletionRequest.Message> messageList =
        messages.getMessages().stream()
            .map(
                message ->
                    ChatCompletionRequest.Message.builder()
                        .setSenderType(message.getRole().toString().toUpperCase())
                        .setText(message.getContent())
                        .build())
            .collect(Collectors.toList());

    ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder =
        ChatCompletionRequest.builder()
            .setModel(miniMaxChatProcessorConfig.getModel())
            .setMessages(messageList);

    // Set optional configuration properties
    miniMaxChatProcessorConfig.getWithEmotion().ifPresent(completionRequestBuilder::setWithEmotion);
    miniMaxChatProcessorConfig.getStream().ifPresent(completionRequestBuilder::setStream);
    miniMaxChatProcessorConfig
        .getUseStandardSse()
        .ifPresent(completionRequestBuilder::setUseStandardSse);
    miniMaxChatProcessorConfig.getBeamWidth().ifPresent(completionRequestBuilder::setBeamWidth);
    miniMaxChatProcessorConfig.getPrompt().ifPresent(completionRequestBuilder::setPrompt);
    miniMaxChatProcessorConfig.getRoleMeta().ifPresent(completionRequestBuilder::setRoleMeta);
    miniMaxChatProcessorConfig
        .getContinueLastMessage()
        .ifPresent(completionRequestBuilder::setContinueLastMessage);
    miniMaxChatProcessorConfig
        .getTokensToGenerate()
        .ifPresent(completionRequestBuilder::setTokensToGenerate);
    miniMaxChatProcessorConfig.getTemperature().ifPresent(completionRequestBuilder::setTemperature);
    miniMaxChatProcessorConfig.getTopP().ifPresent(completionRequestBuilder::setTopP);
    miniMaxChatProcessorConfig
        .getSkipInfoMask()
        .ifPresent(completionRequestBuilder::setSkipInfoMask);

    // Build and return the ChatCompletionRequest
    return completionRequestBuilder.build();
  }
}
