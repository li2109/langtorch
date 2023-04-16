package ai.knowly.langtoch.capability.dag;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.Streams;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CapabilityDAGTest {
  @Test
  public void testProcessGraph_sumInputs() {
    // Arrange
    Node<Integer, Integer> a = new SumInputsProcessingUnit("A", Arrays.asList("B", "C"));
    Node<Integer, Integer> b = new SumInputsProcessingUnit("B", Arrays.asList("D"));
    Node<Integer, Integer> c = new SumInputsProcessingUnit("C", Arrays.asList("D"));
    Node<Integer, Integer> d = new SumInputsProcessingUnit("D", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(a, Integer.class);
    capabilityDAG.addNode(b, Integer.class);
    capabilityDAG.addNode(c, Integer.class);
    capabilityDAG.addNode(d, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act.
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert.
    assertThat(result.get("D")).isEqualTo(2);
  }

  @Test
  public void testProcessGraph_multiplyInputs() {
    // Arrange
    Node<Integer, Integer> a = new MultiplyInputsProcessingUnit("A", Arrays.asList("B", "C"));
    Node<Integer, Integer> b = new MultiplyInputsProcessingUnit("B", Arrays.asList("D"));
    Node<Integer, Integer> c = new MultiplyInputsProcessingUnit("C", Arrays.asList("D"));
    Node<Integer, Integer> d = new MultiplyInputsProcessingUnit("D", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(a, Integer.class);
    capabilityDAG.addNode(b, Integer.class);
    capabilityDAG.addNode(c, Integer.class);
    capabilityDAG.addNode(d, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 2);

    // Act
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert
    assertThat(result.get("D")).isEqualTo(4);
  }

  @Test
  public void testProcessGraph_withInitialMultipleInputs() {
    // Arrange
    Node<Integer, Integer> a = new SumInputsProcessingUnit("A", Arrays.asList("B"));
    Node<Integer, Integer> b = new SumInputsProcessingUnit("B", Arrays.asList("C"));
    Node<Integer, Integer> c = new SumInputsProcessingUnit("C", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(a, Integer.class);
    capabilityDAG.addNode(b, Integer.class);
    capabilityDAG.addNode(c, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);
    initialInputMap.put("B", 2);

    // Act
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert
    assertThat(result.get("C")).isEqualTo(3);
  }

  @Test
  public void testProcessGraph_notDAG() {
    // Arrange: Create a graph with a cycle (A -> B -> C -> A)
    Node<Integer, Integer> a = new NoOpProcessingUnit("A", Arrays.asList("B"));
    Node<Integer, Integer> b = new NoOpProcessingUnit("B", Arrays.asList("C"));
    Node<Integer, Integer> c = new NoOpProcessingUnit("C", Arrays.asList("A"));
    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(a, Integer.class);
    capabilityDAG.addNode(b, Integer.class);
    capabilityDAG.addNode(c, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act & Assert: Expect a RuntimeException due to the cycle in the graph
    assertThrows(RuntimeException.class, () -> capabilityDAG.process(initialInputMap));
  }

  private static final class SumInputsProcessingUnit implements Node<Integer, Integer> {
    private final String id;
    private final List<String> outDegree;

    public SumInputsProcessingUnit(String id, List<String> outDegree) {
      this.id = id;
      this.outDegree = outDegree;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public List<String> getOutDegree() {
      return this.outDegree;
    }

    @Override
    public Integer process(Iterable<Integer> inputs) {
      return Streams.stream(inputs).mapToInt(i -> i).sum();
    }
  }

  private static final class MultiplyInputsProcessingUnit implements Node<Integer, Integer> {
    private final String id;
    private final List<String> outDegree;

    public MultiplyInputsProcessingUnit(String id, List<String> outDegree) {
      this.id = id;
      this.outDegree = outDegree;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public List<String> getOutDegree() {
      return this.outDegree;
    }

    @Override
    public Integer process(Iterable<Integer> inputs) {
      return Streams.stream(inputs).mapToInt(i -> i).reduce(1, (a, b) -> a * b);
    }
  }

  private static final class NoOpProcessingUnit implements Node<Integer, Integer> {
    private final String id;
    private final List<String> outDegree;

    public NoOpProcessingUnit(String id, List<String> outDegree) {
      this.id = id;
      this.outDegree = outDegree;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public List<String> getOutDegree() {
      return this.outDegree;
    }

    @Override
    public Integer process(Iterable<Integer> inputs) {
      return inputs.iterator().next();
    }
  }
}
