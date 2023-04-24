package ai.knowly.langtoch.llm.processor.openai.text;

import static ai.knowly.langtoch.llm.Utils.getOpenAIApiKeyFromEnv;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

/**
 * OpenAI text processor implementation. Handles single text input and output for the OpenAI
 * Language Model.
 */
public class OpenAITextProcessor implements Processor<SingleText, SingleText> {
  // Default model, logger, and default max tokens for this processor
  @VisibleForTesting static final String DEFAULT_MODEL = "text-davinci-003";
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final int DEFAULT_MAX_TOKENS = 2048;

  // OpenAiService instance used for making requests
  private final OpenAiService openAiService;
  // Configuration for the OpenAI Text Processor
  private OpenAITextProcessorConfig openAITextProcessorConfig =
      OpenAITextProcessorConfig.builder()
          .setModel(DEFAULT_MODEL)
          .setMaxTokens(DEFAULT_MAX_TOKENS)
          .build();

  // Constructor with dependency injection
  OpenAITextProcessor(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  // Private constructor used in factory methods
  private OpenAITextProcessor() {
    this.openAiService = new OpenAiService(getOpenAIApiKeyFromEnv(logger));
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAITextProcessor create(OpenAiService openAiService) {
    return new OpenAITextProcessor(openAiService);
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
    CompletionRequest completionRequest =
        OpenAITextProcessorRequestConverter.convert(openAITextProcessorConfig, inputData.getText());

    CompletionResult completion = openAiService.createCompletion(completionRequest);
    String response = completion.getChoices().get(0).getText();
    return SingleText.of(response);
  }
}
