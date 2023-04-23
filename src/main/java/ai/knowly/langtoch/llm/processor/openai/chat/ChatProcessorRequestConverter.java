package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.List;

public final class ChatProcessorRequestConverter {
  public static ChatMessage convertChatMessage(
      ai.knowly.langtoch.llm.schema.chat.ChatMessage chatMessage) {
    return new ChatMessage(chatMessage.getRole().name().toLowerCase(), chatMessage.getMessage());
  }

  public static ChatCompletionRequest convert(
      ChatProcessorConfig chatProcessorConfig,
      List<ai.knowly.langtoch.llm.schema.chat.ChatMessage> messages) {
    ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder =
        ChatCompletionRequest.builder()
            .model(chatProcessorConfig.getModel())
            .messages(
                messages.stream()
                    .map(ChatProcessorRequestConverter::convertChatMessage)
                    .collect(toImmutableList()));

    chatProcessorConfig.getTemperature().ifPresent(completionRequestBuilder::temperature);
    chatProcessorConfig.getTopP().ifPresent(completionRequestBuilder::topP);
    chatProcessorConfig.getN().ifPresent(completionRequestBuilder::n);
    chatProcessorConfig.getStream().ifPresent(completionRequestBuilder::stream);
    completionRequestBuilder.stop(chatProcessorConfig.getStop());
    chatProcessorConfig.getMaxTokens().ifPresent(completionRequestBuilder::maxTokens);
    chatProcessorConfig.getPresencePenalty().ifPresent(completionRequestBuilder::presencePenalty);
    chatProcessorConfig.getFrequencyPenalty().ifPresent(completionRequestBuilder::frequencyPenalty);
    completionRequestBuilder.logitBias(chatProcessorConfig.getLogitBias());
    chatProcessorConfig.getUser().ifPresent(completionRequestBuilder::user);

    return completionRequestBuilder.build();
  }
}
