package ai.knowly.langtoch.llm.processor.openai.text;

import com.theokanning.openai.completion.CompletionRequest;

// Converter class to convert OpenAITextProcessorConfig and a prompt string
// to a CompletionRequest
public final class OpenAITextProcessorRequestConverter {
  // Method to convert OpenAITextProcessorConfig and a prompt string
  // to a CompletionRequest
  public static CompletionRequest convert(
      OpenAITextProcessorConfig openAITextProcessorConfig, String prompt) {
    CompletionRequest completionRequest = new CompletionRequest();

    // Set required configuration properties
    completionRequest.setModel(openAITextProcessorConfig.getModel());
    completionRequest.setPrompt(prompt);

    // Set optional configuration properties
    openAITextProcessorConfig.getSuffix().ifPresent(completionRequest::setSuffix);
    openAITextProcessorConfig.getMaxTokens().ifPresent(completionRequest::setMaxTokens);
    openAITextProcessorConfig.getTemperature().ifPresent(completionRequest::setTemperature);
    openAITextProcessorConfig.getTopP().ifPresent(completionRequest::setTopP);
    openAITextProcessorConfig.getN().ifPresent(completionRequest::setN);
    openAITextProcessorConfig.getStream().ifPresent(completionRequest::setStream);
    openAITextProcessorConfig.getLogprobs().ifPresent(completionRequest::setLogprobs);
    openAITextProcessorConfig.getEcho().ifPresent(completionRequest::setEcho);
    completionRequest.setStop(openAITextProcessorConfig.getStop());
    openAITextProcessorConfig.getPresencePenalty().ifPresent(completionRequest::setPresencePenalty);
    openAITextProcessorConfig
        .getFrequencyPenalty()
        .ifPresent(completionRequest::setFrequencyPenalty);
    openAITextProcessorConfig.getBestOf().ifPresent(completionRequest::setBestOf);
    completionRequest.setLogitBias(openAITextProcessorConfig.getLogitBias());
    openAITextProcessorConfig.getUser().ifPresent(completionRequest::setUser);

    return completionRequest;
  }
}
