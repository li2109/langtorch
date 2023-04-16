package ai.knowly.langtoch.capability.dag;

import java.util.List;

/**
 * Interface representing a node in the CapabilityDAG.
 *
 * @param <I> Input type of the node
 * @param <O> Output type of the node
 */
public interface Node<I, O> {
  String getId();

  List<String> getOutDegree();

  O process(Iterable<I> inputs);
}
