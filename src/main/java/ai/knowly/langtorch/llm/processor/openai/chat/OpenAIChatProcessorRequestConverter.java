package ai.knowly.langtorch.llm.processor.openai.chat;

import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.completion.chat.ChatCompletionRequest;
import java.util.List;

// Converter class to convert OpenAIChatProcessorConfig and a list of chat messages
// to a ChatCompletionRequest
public final class OpenAIChatProcessorRequestConverter {

  private OpenAIChatProcessorRequestConverter() {}

  // Method to convert OpenAIChatProcessorConfig and a list of chat messages
  // to a ChatCompletionRequest
  public static ChatCompletionRequest convert(
      OpenAIChatProcessorConfig openAIChatProcessorConfig,
      List<ai.knowly.langtorch.schema.chat.ChatMessage> messages) {
    ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder =
        ChatCompletionRequest.builder()
            .setModel(openAIChatProcessorConfig.getModel())
            .setMessages(messages);

    // Set optional configuration properties
    openAIChatProcessorConfig.getTemperature().ifPresent(completionRequestBuilder::setTemperature);
    openAIChatProcessorConfig.getTopP().ifPresent(completionRequestBuilder::setTopP);
    openAIChatProcessorConfig.getN().ifPresent(completionRequestBuilder::setN);
    openAIChatProcessorConfig.getStream().ifPresent(completionRequestBuilder::setStream);
    if (!openAIChatProcessorConfig.getStop().isEmpty()) {
      completionRequestBuilder.setStop(openAIChatProcessorConfig.getStop());
    }
    openAIChatProcessorConfig.getMaxTokens().ifPresent(completionRequestBuilder::setMaxTokens);
    openAIChatProcessorConfig
        .getPresencePenalty()
        .ifPresent(completionRequestBuilder::setPresencePenalty);
    openAIChatProcessorConfig
        .getFrequencyPenalty()
        .ifPresent(completionRequestBuilder::setFrequencyPenalty);
    completionRequestBuilder.setLogitBias(openAIChatProcessorConfig.getLogitBias());
    openAIChatProcessorConfig.getUser().ifPresent(completionRequestBuilder::setUser);
    // Build and return the ChatCompletionRequest
    return completionRequestBuilder.build();
  }
}
