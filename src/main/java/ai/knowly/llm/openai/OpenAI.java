package ai.knowly.llm.openai;

import ai.knowly.llm.base.BaseModel;
import com.google.inject.Inject;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionRequest.CompletionRequestBuilder;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

/** OpenAI is a model that uses the OpenAI API to generate text. */
public final class OpenAI extends BaseModel {
  private final OpenAiService openAiService;
  private final CompletionRequestBuilder completionRequest;

  @Inject
  OpenAI(OpenAiService openAiService) {
    this.openAiService = openAiService;
    this.completionRequest = CompletionRequest.builder().maxTokens(2048);
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

  @Override
  public String run(String prompt) {
    CompletionResult completion =
        openAiService.createCompletion(completionRequest.prompt(prompt).build());
    return completion.getChoices().get(0).getText();
  }
}
