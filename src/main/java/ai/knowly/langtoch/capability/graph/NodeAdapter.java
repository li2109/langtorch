package ai.knowly.langtoch.capability.graph;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Interface representing a node in the CapabilityDAG.
 *
 * @param <I> Input type of the node
 * @param <O> Output type of the node
 */
public interface NodeAdapter<I, O> {
  String getId();

  List<String> getOutDegree();

  O process(Iterable<I> inputs) throws ExecutionException, InterruptedException;
}
