package ai.knowly.langtorch.processor.openai.text;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.processor.Processor;
import ai.knowly.langtorch.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * OpenAI text module implementation. Handles single text input and output for the OpenAI Language
 * Model.
 */
public class OpenAITextProcessor implements Processor<SingleText, SingleText> {
  // Default model, logger, and default max tokens for this module
  @VisibleForTesting static final String DEFAULT_MODEL = "text-davinci-003";
  private static final int DEFAULT_MAX_TOKENS = 2048;

  private final OpenAIService openAIService;

  // Configuration for the OpenAI Text Processor
  private OpenAITextProcessorConfig openAITextProcessorConfig =
      OpenAITextProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKENS)
          .build();

  // Constructor with dependency injection
  OpenAITextProcessor(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }

  // Private constructor used in factory methods
  private OpenAITextProcessor() {
    this.openAIService = OpenAIServiceProvider.createOpenAIService();
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAITextProcessor create(OpenAIService openAIService) {
    return new OpenAITextProcessor(openAIService);
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAITextProcessor create(String openAIKey) {
    return new OpenAITextProcessor(OpenAIServiceProvider.createOpenAIService(openAIKey));
  }

  // Factory method to create a new OpenAITextProcessor instance
  public static OpenAITextProcessor create() {
    return new OpenAITextProcessor();
  }

  // Method to set the module configuration
  public OpenAITextProcessor withConfig(OpenAITextProcessorConfig openAITextProcessorConfig) {
    this.openAITextProcessorConfig = openAITextProcessorConfig;
    return this;
  }

  // Method to run the module with the given input and return the output text
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
