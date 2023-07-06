package ai.knowly.langtorch.tool;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.util.concurrent.Futures.immediateFuture;

import ai.knowly.langtorch.agent.LLMTool;
import com.google.common.base.Splitter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

/** Add all integers in a list. */
public class IntegerListAdderTool implements LLMTool<List<Integer>, Integer> {
  @Override
  public List<Integer> preProcess(String inputData) {
    return Splitter.on(",").splitToList(inputData).stream()
        .map(Integer::parseInt)
        .collect(toImmutableList());
  }

  @Override
  public String postProcess(Integer outputData) {
    return outputData.toString();
  }

  @Override
  public String getName() {
    return "IntegerListAdder";
  }

  @Override
  public String getDescription() {
    return "Add all integers in a list.";
  }

  @Override
  public Integer run(List<Integer> inputData) {
    return inputData.stream().mapToInt(i -> i).sum();
  }

  @Override
  public ListenableFuture<Integer> runAsync(List<Integer> inputData) {
    return immediateFuture(run(inputData));
  }
}
