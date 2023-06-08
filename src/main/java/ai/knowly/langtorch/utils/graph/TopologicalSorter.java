package ai.knowly.langtorch.utils.graph;

import java.util.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/** Topological sort of DAG. */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TopologicalSorter {
  public static List<String> topologicalSort(Map<String, List<String>> graph) {
    Map<String, Integer> inDegree = new HashMap<>();
    Queue<String> queue = new LinkedList<>();
    Deque<String> stack = new ArrayDeque<>();
    List<String> result = new ArrayList<>();

    // Initialize in-degrees
    for (String node : graph.keySet()) {
      inDegree.putIfAbsent(node, 0);
      for (String neighbor : graph.get(node)) {
        inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
      }
    }

    // Add nodes with in-degree 0 to the queue
    for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.add(entry.getKey());
      }
    }

    // Process nodes in the queue
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

    // Check if a topological sort is possible
    if (stack.size() != inDegree.size()) {
      throw new DAGViolationException(
          "The graph has at least one cycle, so a topological sort is not possible.");
    }

    while (!stack.isEmpty()) {
      result.add(stack.pop());
    }

    return result;
  }
}
