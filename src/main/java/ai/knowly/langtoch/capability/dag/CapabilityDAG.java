package ai.knowly.langtoch.capability.dag;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Class representing a directed acyclic graph (DAG) of capabilities. */
public class CapabilityDAG {
  private final HashMap<String, Node<?, ?>> nodes = new HashMap<>();
  private final Multimap<String, Object> inputMap = ArrayListMultimap.create();
  private final HashMap<String, Object> outputMap = new HashMap<>();
  private final Multimap<String, String> inDegreeMap = ArrayListMultimap.create();
  private final HashMap<String, TypeToken<?>> inputTypes = new HashMap<>();

  /**
   * Add a node to the CapabilityDAG.
   *
   * @param node Node to be added
   * @param inputType Class object representing the input type of the node
   * @param <I> Input type of the node
   * @param <O> Output type of the node
   */
  public <I, O> void addNode(Node<I, O> node, Class<I> inputType) {
    nodes.put(node.getId(), node);
    inputTypes.put(node.getId(), TypeToken.of(inputType));
    for (String outDegree : node.getOutDegree()) {
      inDegreeMap.put(outDegree, node.getId());
    }
  }

  /**
   * Process the CapabilityDAG with the given initial inputs.
   *
   * @param initialInputMap Map of node IDs to their initial input values
   * @return Map of end node IDs to their final output values
   */
  public Map<String, Object> process(Map<String, Object> initialInputMap) {
    for (Map.Entry<String, Object> entry : initialInputMap.entrySet()) {
      setInitialInput(entry.getKey(), entry.getValue());
    }
    List<String> sortedList = topologicalSort();

    for (String id : sortedList) {
      Node<?, ?> node = nodes.get(id);
      Collection<Object> input = inputMap.get(id);
      Object output = processNode(node, input);
      addOutput(id, output);
      for (String outDegree : node.getOutDegree()) {
        addInput(outDegree, output);
      }
    }

    Map<String, Object> result = new HashMap<>();
    for (String id : getEndNodeIds()) {
      result.put(id, outputMap.get(id));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private <I, O> O processNode(Node<I, O> node, Collection<Object> input) {
    Iterable<I> typedInput = (Iterable<I>) input;
    return node.process(typedInput);
  }

  public Object getOutput(String id) {
    return outputMap.get(id);
  }

  private List<String> getEndNodeIds() {
    List<String> endNodeIds = new ArrayList<>();
    for (Node<?, ?> node : nodes.values()) {
      if (node.getOutDegree().isEmpty()) {
        endNodeIds.add(node.getId());
      }
    }
    return endNodeIds;
  }

  private void setInitialInput(String id, Object input) {
    TypeToken<?> expectedType = inputTypes.get(id);
    if (!expectedType.isSupertypeOf(input.getClass())) {
      throw new IllegalArgumentException(
          "Input type for node " + id + " does not match the expected type.");
    }
    inputMap.put(id, input);
  }

  private void addOutput(String id, Object output) {
    outputMap.put(id, output);
  }

  private void addInput(String id, Object input) {
    TypeToken<?> expectedType = inputTypes.get(id);
    if (!expectedType.isSupertypeOf(input.getClass())) {
      throw new IllegalArgumentException(
          "Input type for node " + id + " does not match the expected type.");
    }
    inputMap.put(id, input);
  }

  /**
   * Perform a topological sort on the graph to determine the correct order of node processing.
   *
   * @return A list of node IDs in the order they should be processed
   */
  private List<String> topologicalSort() {
    List<String> sortedList = new ArrayList<>();
    Queue<String> queue = new LinkedList<>();
    HashMap<String, Integer> inDegreeCount = new HashMap<>();
    for (String node : nodes.keySet()) {
      inDegreeCount.put(node, inDegreeMap.get(node).size());
      if (inDegreeCount.get(node) == 0) {
        queue.add(node);
      }
    }
    while (!queue.isEmpty()) {
      String current = queue.poll();
      sortedList.add(current);

      for (String neighbor : nodes.get(current).getOutDegree()) {
        int updatedInDegree = inDegreeCount.get(neighbor) - 1;
        inDegreeCount.put(neighbor, updatedInDegree);

        if (updatedInDegree == 0) {
          queue.add(neighbor);
        }
      }
    }

    if (sortedList.size() != nodes.size()) {
      throw new RuntimeException("The graph contains a cycle and cannot be topologically sorted.");
    }

    return sortedList;
  }
}
