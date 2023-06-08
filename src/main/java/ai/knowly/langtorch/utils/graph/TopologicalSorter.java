package ai.knowly.langtorch.utils.graph;

import java.util.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/** Class to perform topological sort on a Directed Acyclic Graph (DAG). */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TopologicalSorter {
  public static List<String> topologicalSort(Map<String, List<String>> graph) {
    // Create a map to store the in-degree of each node
    Map<String, Integer> inDegree = initializeInDegree(graph);

    // Create a queue and enqueue all nodes of in-degree 0
    Queue<String> queue = getZeroInDegreeNodes(inDegree);

    // Create a stack to store the result and process nodes in the queue
    Deque<String> stack = processNodes(graph, inDegree, queue);

    // Check if a topological sort is possible (i.e., the graph is a DAG)
    if (!isTopologicalSortPossible(inDegree, stack)) {
      throw new DAGViolationException(
          "The graph has at least one cycle, so a topological sort is not possible.");
    }

    // Get the result from the stack
    return getResultFromStack(stack);
  }

  // Initialize in-degrees
  private static Map<String, Integer> initializeInDegree(Map<String, List<String>> graph) {
    Map<String, Integer> inDegree = new HashMap<>();
    graph
        .entrySet()
        .forEach(
            entry -> {
              inDegree.putIfAbsent(entry.getKey(), 0);
              for (String neighbor : entry.getValue()) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
              }
            });
    return inDegree;
  }

  // Get nodes with in-degree 0
  private static Queue<String> getZeroInDegreeNodes(Map<String, Integer> inDegree) {
    Queue<String> queue = new LinkedList<>();
    for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.add(entry.getKey());
      }
    }
    return queue;
  }

  // Process nodes in the queue
  private static Deque<String> processNodes(
      Map<String, List<String>> graph, Map<String, Integer> inDegree, Queue<String> queue) {
    Deque<String> stack = new ArrayDeque<>();
    while (!queue.isEmpty()) {
      String node = queue.poll();
      stack.push(node);
      if (graph.containsKey(node)) {
        for (String neighbor : graph.get(node)) {
          inDegree.put(neighbor, inDegree.get(neighbor) - 1);
          if (inDegree.get(neighbor) == 0) {
            queue.add(neighbor);
          }
        }
      }
    }
    return stack;
  }

  // Check if a topological sort is possible
  private static boolean isTopologicalSortPossible(
      Map<String, Integer> inDegree, Deque<String> stack) {
    return stack.size() == inDegree.size();
  }

  // Get the result from the stack
  private static List<String> getResultFromStack(Deque<String> stack) {
    List<String> result = new ArrayList<>();
    while (!stack.isEmpty()) {
      result.add(stack.pop());
    }
    return result;
  }
}
