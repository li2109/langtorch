package ai.knowly.chain;

import ai.knowly.llm.base.BaseModel;
import ai.knowly.prompt.PromptTemplate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Optional;

/**
 * A class for the LLM chain. It contains a base model and a prompt template where formatted prompt
 * is fed into the model.
 */
public final class LLMChain {

  private final BaseModel baseModel;
  private final PromptTemplate promptTemplate;

  LLMChain(BaseModel baseModel, PromptTemplate promptTemplate) {
    this.baseModel = baseModel;
    this.promptTemplate = promptTemplate;
  }

  public String simpleRun(String variableValue) {
    ImmutableList<String> variableNames = promptTemplate.extractVariableNames();
    if (variableNames.size() != 1) {
      throw new RuntimeException(
          "Prompt template should only contain one variable for simple run.");
    }
    return run(
        promptTemplate.addVariableValuePair(
            Iterables.getOnlyElement(variableNames), variableValue));
  }

  public String run() {
    return run(this.promptTemplate);
  }

  private String run(PromptTemplate promptTemplate) {
    Optional<String> formattedPrompt = promptTemplate.format();
    if (formattedPrompt.isEmpty()) {
      throw new RuntimeException("prompt is not present.");
    }
    return baseModel.run(formattedPrompt.get());
  }
}
