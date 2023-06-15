package ai.knowly.langtorch.processor.cohere.generate;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.cohere.CohereApiService;
import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateResponse;
import ai.knowly.langtorch.processor.Processor;
import ai.knowly.langtorch.processor.ProcessorExecutionException;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

/** Processor for Cohere.ai text generation service.*/
public class CohereGenerateProcessor implements Processor<SingleText, SingleText> {

  private final CohereApiService cohereApiService;
  private final CohereGenerateProcessorConfig config;

  @Inject
  CohereGenerateProcessor(CohereApiService cohereApiService, CohereGenerateProcessorConfig config) {
    this.cohereApiService = cohereApiService;
    this.config = config;
  }

  @Override
  public SingleText run(SingleText inputData) {
    CohereGenerateResponse response =
        cohereApiService.generate(
            CohereGenerateRequestConverter.convert(inputData.getText(), config));
    if (response.getGenerations().isEmpty()) {
      throw new ProcessorExecutionException("Receive empty generations from cohere.ai.");
    }
    return SingleText.of(response.getGenerations().get(0).getText());
  }

  @Override
  public ListenableFuture<SingleText> runAsync(SingleText inputData) {
    ListenableFuture<CohereGenerateResponse> responseFuture =
        cohereApiService.generateAsync(
            CohereGenerateRequestConverter.convert(inputData.getText(), config));
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
