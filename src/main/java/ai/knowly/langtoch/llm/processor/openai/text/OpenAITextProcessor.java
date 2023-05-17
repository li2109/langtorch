package ai.knowly.langtoch.llm.processor.openai.text;

import static ai.knowly.langtoch.llm.Utils.singleToCompletableFuture;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAiApi;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionRequest;
import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtoch.schema.io.SingleText;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.flogger.FluentLogger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * OpenAI text processor implementation. Handles single text input and output for the OpenAI
 * Language Model.
 */
public class OpenAITextProcessor implements Processor<SingleText, SingleText> {
  // Default model, logger, and default max tokens for this processor
  @VisibleForTesting static final String DEFAULT_MODEL = "text-davinci-003";
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final int DEFAULT_MAX_TOKENS = 2048;

  // OpenAiApi instance used for making requests
  private final OpenAiApi openAiApi;

  // Configuration for the OpenAI Text Processor
  private OpenAITextProcessorConfig openAITextProcessorConfig =
      OpenAITextProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKENS)
          .build();

  // Constructor with dependency injection
  OpenAITextProcessor(OpenAiApi openAiApi) {
    this.openAiApi = openAiApi;
  }

  // Private constructor used in factory methods
  private OpenAITextProcessor() {
    this.openAiApi = OpenAIServiceProvider.createOpenAiAPI();
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAITextProcessor create(OpenAiApi openAiApi) {
    return new OpenAITextProcessor(openAiApi);
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAITextProcessor create(String openAIKey) {
    return new OpenAITextProcessor(OpenAIServiceProvider.createOpenAiAPI(openAIKey));
  }

  // Factory method to create a new OpenAITextProcessor instance
  public static OpenAITextProcessor create() {
    return new OpenAITextProcessor();
  }

  // Method to set the processor configuration
  public OpenAITextProcessor withConfig(OpenAITextProcessorConfig openAITextProcessorConfig) {
    this.openAITextProcessorConfig = openAITextProcessorConfig;
    return this;
  }

  // Method to run the processor with the given input and return the output text
  @Override
  public SingleText run(SingleText inputData) {
    try {
      return runAsync(CompletableFuture.completedFuture(inputData)).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.atWarning().withCause(e).log("Error running OpenAI Text Processor");
      throw new RuntimeException(e);
    }
  }

  @Override
  public CompletableFuture<SingleText> runAsync(CompletableFuture<SingleText> inputData) {
    return inputData.thenCompose(
        data -> {
          CompletionRequest completionRequest =
              OpenAITextProcessorRequestConverter.convert(
                  openAITextProcessorConfig, data.getText());

          return singleToCompletableFuture(openAiApi.createCompletion(completionRequest))
              .thenApply(
                  completion -> {
                    String response = completion.getChoices().get(0).getText();
                    return SingleText.of(response);
                  });
        });
  }
}
