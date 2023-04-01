package ai.knowly.chain;

import ai.knowly.llm.base.BaseModel;
import ai.knowly.prompt.PromptTemplate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LLMChain {

  private final BaseModel baseModel;
  private final PromptTemplate promptTemplate;

  LLMChain(BaseModel baseModel, PromptTemplate promptTemplate) {
    this.baseModel = baseModel;
    this.promptTemplate = promptTemplate;
  }

  public static Optional<String> extractVariableName(String input) {
    String pattern = "\\{([^}]*)\\}";
    Pattern compiledPattern = Pattern.compile(pattern);
    Matcher matcher = compiledPattern.matcher(input);

    while (matcher.find()) {
      return Optional.of(matcher.group(1));
    }
    return Optional.empty();
  }

  public String simpleRun(String variableValue) {
    String template = promptTemplate.getTemplate();
    Optional<String> variableName = extractVariableName(template);
    if (variableName.isEmpty()) {
      throw new RuntimeException("Please provide at least one variable.");
    }
    return run(promptTemplate.addVariable(variableName.get(), variableValue));
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
