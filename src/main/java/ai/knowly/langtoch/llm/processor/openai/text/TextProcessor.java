package ai.knowly.langtoch.llm.processor.openai.text;

import static ai.knowly.langtoch.llm.Utils.getOpenAIApiKeyFromEnv;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.input.SingleTextInput;
import ai.knowly.langtoch.llm.schema.io.output.SingleTextOutput;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

public class TextProcessor implements Processor<SingleTextInput, SingleTextOutput> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final OpenAiService openAiService;
  private TextProcessorConfig textProcessorConfig = TextProcessorConfig.builder().build();

  TextProcessor(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  private TextProcessor() {
    this.openAiService = new OpenAiService(getOpenAIApiKeyFromEnv(logger));
  }

  public static TextProcessor create(OpenAiService openAiService) {
    return new TextProcessor(openAiService);
  }

  public static TextProcessor create() {
    return new TextProcessor();
  }

  public TextProcessor withConfig(TextProcessorConfig textProcessorConfig) {
    this.textProcessorConfig = textProcessorConfig;
    return this;
  }

  @Override
  public SingleTextOutput run(SingleTextInput inputData) {
    CompletionRequest completionRequest =
        TextProcessorRequestConverter.convert(textProcessorConfig, inputData.getText());

    CompletionResult completion = openAiService.createCompletion(completionRequest);
    String response = completion.getChoices().get(0).getText();
    return SingleTextOutput.of(response);
  }
}
