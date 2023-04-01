package ai.knowly.chain;

import com.google.common.collect.ImmutableList;
import lombok.Builder;

@Builder(setterPrefix = "set")
public final class SequentialChain {
  private final ImmutableList<LLMChain> chains;

  public String run(String variableValue) {
    if (chains.isEmpty()) {
      throw new RuntimeException("No chains to run.");
    }
    String result = variableValue;
    for (LLMChain chain : chains) {
      result = chain.simpleRun(result);
    }
    return result;
  }
}
