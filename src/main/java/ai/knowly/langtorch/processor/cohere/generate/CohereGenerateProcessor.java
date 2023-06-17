package ai.knowly.langtorch.processor.cohere.generate;

import ai.knowly.langtorch.llm.cohere.CohereAIService;
import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateRequest;
import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateResponse;
import ai.knowly.langtorch.processor.ProcessorExecutionException;
import ai.knowly.langtorch.processor.Processor;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

/** Processor for Cohere.ai text generation service. */
public class CohereGenerateProcessor implements Processor<SingleText, SingleText> {

  private final CohereAIService cohereAIService;

  @Inject
  CohereGenerateProcessor(CohereAIService cohereAIService) {
    this.cohereAIService = cohereAIService;
  }

  @Override
  public SingleText run(SingleText inputData) {
    CohereGenerateResponse response =
        cohereAIService.generate(
            CohereGenerateRequest.builder().prompt(inputData.getText()).build());
    if (response.getGenerations().isEmpty()) {
      throw new ProcessorExecutionException("Receive empty generations from cohere.ai.");
    }
    return SingleText.of(response.getGenerations().get(0).getText());
  }

  @Override
  public ListenableFuture<SingleText> runAsync(SingleText inputData) {
    ListenableFuture<CohereGenerateResponse> responseFuture =
        cohereAIService.generateAsync(
            CohereGenerateRequest.builder().prompt(inputData.getText()).build());
    return Futures.transform(
        responseFuture,
        response -> {
          if (response.getGenerations().isEmpty()) {
            throw new ProcessorExecutionException("Receive empty generations from cohere.ai.");
          }
          return SingleText.of(response.getGenerations().get(0).getText());
        },
        directExecutor());
  }
}
