package ai.knowly.langtoch.chain;

import com.google.common.collect.ImmutableList;
import lombok.Builder;

/**
 * A sequential chain of LLMChains. Concatenating with multiple LLMChains in sequence where the
 * output of one LLMChain is the input of the next.
 *
 * <p>Currently, it only supports one variable in the prompt template.
 */
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
