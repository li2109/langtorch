package ai.knowly.langtoch.llm.providers.openai;

import ai.knowly.langtoch.llm.base.BaseModel;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionRequest.CompletionRequestBuilder;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;
import javax.inject.Inject;

/** OpenAI is a model that uses the OpenAI API to generate text. */
public class OpenAI extends BaseModel {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final String DEFAULT_MODEL = "text-davinci-003";
  private final int DEFAULT_MAX_TOKENS = 2048;
  private final OpenAiService openAiService;
  private final CompletionRequestBuilder completionRequest =
      CompletionRequest.builder().maxTokens(DEFAULT_MAX_TOKENS).model(DEFAULT_MODEL);

  @Inject
  OpenAI(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  public OpenAI(String apiKey) {
    Utils.logPartialApiKey(logger, apiKey);
    this.openAiService = new OpenAiService(apiKey);
  }

  public OpenAI() {
    this.openAiService = new OpenAiService(getApiKeyFromEnv());
  }

  public OpenAI setMaxTokens(int maxTokens) {
    completionRequest.maxTokens(maxTokens);
    return this;
  }

  public OpenAI setModel(String model) {
    completionRequest.model(model);
    return this;
  }

  public OpenAI setTemperature(double temperature) {
    completionRequest.temperature(temperature);
    return this;
  }

  /**
   * Runs the model and returns the generated text. Underlying, this uses the OpenAI completion API.
   */
  @Override
  public String run(String prompt) {
    CompletionResult completion =
        openAiService.createCompletion(completionRequest.prompt(prompt).build());
    return completion.getChoices().get(0).getText();
  }

  private String getApiKeyFromEnv() {
    Dotenv dotenv = Dotenv.load();
    String openaiApiKey = Objects.requireNonNull(dotenv.get("OPENAI_API_KEY"));
    Utils.logPartialApiKey(logger, openaiApiKey);
    return openaiApiKey;
  }
}
