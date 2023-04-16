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

    class SumInputsProcessingUnit extends ProcessingUnit {
      public SumInputsProcessingUnit(String id, List<String> outDegree) {
        super(id, outDegree);
      }

      @Override
      public Object process(Iterable<Object> inputs) {
        return Streams.stream(inputs).mapToInt(i -> (int) i).sum();
      }
    }
    // Arrange
    ProcessingUnit a = new SumInputsProcessingUnit("A", Arrays.asList("B", "C"));
    ProcessingUnit b = new SumInputsProcessingUnit("B", Arrays.asList("D"));
    ProcessingUnit c = new SumInputsProcessingUnit("C", Arrays.asList("D"));
    ProcessingUnit d = new SumInputsProcessingUnit("D", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG(Arrays.asList(a, b, c, d));

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act.
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert.
    assertThat(result.get("D")).isEqualTo(2);
  }

  @Test
  public void testProcessGraph_multiplyInputs() {
    class MultiplyInputsProcessingUnit extends ProcessingUnit {
      public MultiplyInputsProcessingUnit(String id, List<String> outDegree) {
        super(id, outDegree);
      }

      @Override
      public Object process(Iterable<Object> inputs) {
        return Streams.stream(inputs).mapToInt(i -> (int) i).reduce(1, (a, b) -> a * b);
      }
    }

    // Arrange
    ProcessingUnit a = new MultiplyInputsProcessingUnit("A", Arrays.asList("B", "C"));
    ProcessingUnit b = new MultiplyInputsProcessingUnit("B", Arrays.asList("D"));
    ProcessingUnit c = new MultiplyInputsProcessingUnit("C", Arrays.asList("D"));
    ProcessingUnit d = new MultiplyInputsProcessingUnit("D", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG(Arrays.asList(a, b, c, d));

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 2);

    // Act
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert
    assertThat(result.get("D")).isEqualTo(4);
  }

  @Test
  public void testProcessGraph_withInitialMultipleInputs() {
    class SumInputsProcessingUnit extends ProcessingUnit {
      public SumInputsProcessingUnit(String id, List<String> outDegree) {
        super(id, outDegree);
      }

      @Override
      public Object process(Iterable<Object> inputs) {
        return Streams.stream(inputs).mapToInt(i -> (int) i).sum();
      }
    }

    // Arrange
    ProcessingUnit a = new SumInputsProcessingUnit("A", Arrays.asList("B"));
    ProcessingUnit b = new SumInputsProcessingUnit("B", Arrays.asList("C"));
    ProcessingUnit c = new SumInputsProcessingUnit("C", Arrays.asList());
    CapabilityDAG capabilityDAG = new CapabilityDAG(Arrays.asList(a, b, c));

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);
    initialInputMap.put("B", 2);

    // Act
    Map<String, Object> result = capabilityDAG.process(initialInputMap);

    // Assert
    assertThat(result.get("C")).isEqualTo(3);
  }

  public void testProcessGraph_notDAG() {
    class NoOpProcessingUnit extends ProcessingUnit {
      public NoOpProcessingUnit(String id, List<String> outDegree) {
        super(id, outDegree);
      }

      @Override
      public Object process(Iterable<Object> inputs) {
        return inputs.iterator().next();
      }
    }

    // Arrange: Create a graph with a cycle (A -> B -> C -> A)
    ProcessingUnit a = new NoOpProcessingUnit("A", Arrays.asList("B"));
    ProcessingUnit b = new NoOpProcessingUnit("B", Arrays.asList("C"));
    ProcessingUnit c = new NoOpProcessingUnit("C", Arrays.asList("A"));
    CapabilityDAG capabilityDAG = new CapabilityDAG(Arrays.asList(a, b, c));

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act & Assert: Expect a RuntimeException due to the cycle in the graph
    assertThrows(RuntimeException.class, () -> capabilityDAG.process(initialInputMap));
  }
}
