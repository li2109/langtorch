package ai.knowly.langtoch.llm.processor.openai.text;

import com.theokanning.openai.completion.CompletionRequest;

/** Converts a {@link TextProcessorConfig} and prompt to a {@link CompletionRequest}. */
public final class TextProcessorRequestConverter {
  public static CompletionRequest convert(TextProcessorConfig textProcessorConfig, String prompt) {
    CompletionRequest completionRequest = new CompletionRequest();

    completionRequest.setModel(textProcessorConfig.getModel());
    completionRequest.setPrompt(prompt);
    textProcessorConfig.getSuffix().ifPresent(completionRequest::setSuffix);
    textProcessorConfig.getMaxTokens().ifPresent(completionRequest::setMaxTokens);
    textProcessorConfig.getTemperature().ifPresent(completionRequest::setTemperature);
    textProcessorConfig.getTopP().ifPresent(completionRequest::setTopP);
    textProcessorConfig.getN().ifPresent(completionRequest::setN);
    textProcessorConfig.getStream().ifPresent(completionRequest::setStream);
    textProcessorConfig.getLogprobs().ifPresent(completionRequest::setLogprobs);
    textProcessorConfig.getEcho().ifPresent(completionRequest::setEcho);
    completionRequest.setStop(textProcessorConfig.getStop());
    textProcessorConfig.getPresencePenalty().ifPresent(completionRequest::setPresencePenalty);
    textProcessorConfig.getFrequencyPenalty().ifPresent(completionRequest::setFrequencyPenalty);
    textProcessorConfig.getBestOf().ifPresent(completionRequest::setBestOf);
    completionRequest.setLogitBias(textProcessorConfig.getLogitBias());
    textProcessorConfig.getUser().ifPresent(completionRequest::setUser);

    return completionRequest;
  }
}
