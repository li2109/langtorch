package ai.knowly.langtorch.util.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.knowly.langtorch.utils.graph.DAGViolationException;
import ai.knowly.langtorch.utils.graph.TopologicalSorter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TopologicalSorterTest {

  @Test
  void testValidTopologicalSort() {
    // Arrange.
    Map<String, List<String>> graph = new HashMap<>();
    graph.put("A", Arrays.asList("B", "C"));
    graph.put("B", Collections.singletonList("D"));
    graph.put("C", Collections.singletonList("D"));
    graph.put("D", new ArrayList<>());
    List<List<String>> validOutcomes =
        Arrays.asList(Arrays.asList("D", "C", "B", "A"), Arrays.asList("D", "B", "C", "A"));

    // Act.
    List<String> sorted = TopologicalSorter.topologicalSort(graph);

    // Assert.
    assertThat(validOutcomes).contains(sorted);
  }

  @Test
  void testEmptyGraph() {
    // Arrange.
    Map<String, List<String>> graph = new HashMap<>();

    // Act.
    List<String> sorted = TopologicalSorter.topologicalSort(graph);

    // Assert.
    assertThat(sorted).isEmpty();
  }

  @Test
  void testGraphWithCycle() {
    // Arrange.
    Map<String, List<String>> graph = new HashMap<>();
    graph.put("A", Collections.singletonList("B"));
    graph.put("B", Collections.singletonList("A"));

    // Act & Assert.
    assertThrows(
        DAGViolationException.class,
        () -> {
          TopologicalSorter.topologicalSort(graph);
        });
  }

  @Test
  void testSingleElementGraph() {
    // Arrange.
    Map<String, List<String>> graph = new HashMap<>();
    graph.put("A", new ArrayList<>());

    // Act.
    List<String> sorted = TopologicalSorter.topologicalSort(graph);

    // Assert.
    assertThat(sorted).containsExactly("A").inOrder();
  }
}
