package ai.knowly.langtorch.processor.module.openai.text;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

/**
 * OpenAI text module implementation. Handles single text input and output for the OpenAI Language
 * Model.
 */
public class OpenAITextProcessor implements Processor<SingleText, SingleText> {
  private final OpenAIService openAIService;
  // Configuration for the OpenAI Text Processor
  private final OpenAITextProcessorConfig openAITextProcessorConfig;

  @Inject
  public OpenAITextProcessor(
      OpenAIService openAIService, OpenAITextProcessorConfig openAITextProcessorConfig) {
    this.openAIService = openAIService;
    this.openAITextProcessorConfig = openAITextProcessorConfig;
  }

  @Override
  public SingleText run(SingleText inputData) {
    CompletionRequest completionRequest =
        OpenAITextProcessorRequestConverter.convert(openAITextProcessorConfig, inputData.getText());
    CompletionResult completion = openAIService.createCompletion(completionRequest);
    return SingleText.of(completion.getChoices().get(0).getText());
  }

  @Override
  public ListenableFuture<SingleText> runAsync(SingleText inputData) {
    CompletionRequest completionRequest =
        OpenAITextProcessorRequestConverter.convert(openAITextProcessorConfig, inputData.getText());
    return FluentFuture.from(openAIService.createCompletionAsync(completionRequest))
        .transform(
            (CompletionResult completion) ->
                SingleText.of(completion.getChoices().get(0).getText()),
            directExecutor());
  }
}
