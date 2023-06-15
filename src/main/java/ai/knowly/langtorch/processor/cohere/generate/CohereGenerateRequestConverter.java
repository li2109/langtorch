package ai.knowly.langtorch.processor.cohere.generate;

import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateRequest;

public class CohereGenerateRequestConverter {

  private CohereGenerateRequestConverter() {}

  public static CohereGenerateRequest convert(
      String prompt, CohereGenerateProcessorConfig cohereGenerateProcessorConfig) {
    CohereGenerateRequest.Builder cohereGenerateRequestBuilder =
        CohereGenerateRequest.builder().prompt(prompt);

    // Set optional configuration properties
    cohereGenerateProcessorConfig
        .getTemperature()
        .ifPresent(cohereGenerateRequestBuilder::temperature);
    cohereGenerateProcessorConfig.getP().ifPresent(cohereGenerateRequestBuilder::p);
    cohereGenerateProcessorConfig.getK().ifPresent(cohereGenerateRequestBuilder::k);
    cohereGenerateProcessorConfig
        .getNumGenerations()
        .ifPresent(cohereGenerateRequestBuilder::numGenerations);
    if (!cohereGenerateProcessorConfig.getEndSequences().isEmpty()) {
      cohereGenerateRequestBuilder.endSequences(cohereGenerateProcessorConfig.getEndSequences());
    }
    if (!cohereGenerateProcessorConfig.getStopSequences().isEmpty()) {
      cohereGenerateRequestBuilder.stopSequences(cohereGenerateProcessorConfig.getStopSequences());
    }
    cohereGenerateProcessorConfig.getMaxTokens().ifPresent(cohereGenerateRequestBuilder::maxTokens);
    cohereGenerateProcessorConfig
        .getPresencePenalty()
        .ifPresent(cohereGenerateRequestBuilder::presencePenalty);
    cohereGenerateProcessorConfig
        .getFrequencyPenalty()
        .ifPresent(cohereGenerateRequestBuilder::frequencyPenalty);
    cohereGenerateRequestBuilder.logitBias(cohereGenerateProcessorConfig.getLogitBias());
    cohereGenerateProcessorConfig
        .getReturnLikelihoods()
        .ifPresent(
            likelihoods -> cohereGenerateRequestBuilder.returnLikelihoods(likelihoods.toString()));
    cohereGenerateProcessorConfig
        .getTruncate()
        .ifPresent(truncate -> cohereGenerateRequestBuilder.truncate(truncate.toString()));
    return cohereGenerateRequestBuilder.build();
  }
}
