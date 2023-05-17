package ai.knowly.langtoch.capability.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAiService;
import com.google.common.collect.Streams;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class CapabilityGraphTest {
  @Mock OpenAiService openAIChat;

  @Test
  void testProcessGraph_sumInputs() throws ExecutionException, InterruptedException {
    // Arrange
    NodeAdapter<Integer, Integer> a = new SumInputsProcessingUnit("A", Arrays.asList("B", "C"));
    NodeAdapter<Integer, Integer> b = new SumInputsProcessingUnit("B", List.of("D"));
    NodeAdapter<Integer, Integer> c = new SumInputsProcessingUnit("C", List.of("D"));
    NodeAdapter<Integer, Integer> d = new SumInputsProcessingUnit("D", List.of());
    CapabilityGraph capabilityGraph = CapabilityGraph.create();
    capabilityGraph.addNode(a, Integer.class);
    capabilityGraph.addNode(b, Integer.class);
    capabilityGraph.addNode(c, Integer.class);
    capabilityGraph.addNode(d, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act.
    Map<String, Object> result = capabilityGraph.process(initialInputMap);

    // Assert.
    assertThat(result.get("D")).isEqualTo(2);
  }

  @Test
  void testProcessGraph_multiplyInputs() throws ExecutionException, InterruptedException {
    // Arrange
    NodeAdapter<Integer, Integer> a =
        new MultiplyInputsProcessingUnit("A", Arrays.asList("B", "C"));
    NodeAdapter<Integer, Integer> b = new MultiplyInputsProcessingUnit("B", List.of("D"));
    NodeAdapter<Integer, Integer> c = new MultiplyInputsProcessingUnit("C", List.of("D"));
    NodeAdapter<Integer, Integer> d = new MultiplyInputsProcessingUnit("D", List.of());
    CapabilityGraph capabilityGraph = CapabilityGraph.create();
    capabilityGraph.addNode(a, Integer.class);
    capabilityGraph.addNode(b, Integer.class);
    capabilityGraph.addNode(c, Integer.class);
    capabilityGraph.addNode(d, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 2);

    // Act
    Map<String, Object> result = capabilityGraph.process(initialInputMap);

    // Assert
    assertThat(result.get("D")).isEqualTo(4);
  }

  @Test
  void testProcessGraph_withInitialMultipleInputs()
      throws ExecutionException, InterruptedException {
    // Arrange
    NodeAdapter<Integer, Integer> a = new SumInputsProcessingUnit("A", List.of("B"));
    NodeAdapter<Integer, Integer> b = new SumInputsProcessingUnit("B", List.of("C"));
    NodeAdapter<Integer, Integer> c = new SumInputsProcessingUnit("C", List.of());
    CapabilityGraph capabilityGraph = CapabilityGraph.create();
    capabilityGraph.addNode(a, Integer.class);
    capabilityGraph.addNode(b, Integer.class);
    capabilityGraph.addNode(c, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);
    initialInputMap.put("B", 2);

    // Act
    Map<String, Object> result = capabilityGraph.process(initialInputMap);

    // Assert
    assertThat(result.get("C")).isEqualTo(3);
  }

  @Test
  public void testProcessGraph_notDAG() {
    // Arrange: Create a graph with a cycle (A -> B -> C -> A)
    NodeAdapter<Integer, Integer> a = new NoOpProcessingUnit("A", List.of("B"));
    NodeAdapter<Integer, Integer> b = new NoOpProcessingUnit("B", List.of("C"));
    NodeAdapter<Integer, Integer> c = new NoOpProcessingUnit("C", List.of("A"));
    CapabilityGraph capabilityGraph = CapabilityGraph.create();
    capabilityGraph.addNode(a, Integer.class);
    capabilityGraph.addNode(b, Integer.class);
    capabilityGraph.addNode(c, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act & Assert: Expect a RuntimeException due to the cycle in the graph
    assertThrows(RuntimeException.class, () -> capabilityGraph.process(initialInputMap));
  }

  //
  //  @Test
  //  public void testConcatenatedNodeAdapters() {
  //    // Arrange.
  //    String query1 = "Write a creative name for a sport car company.";
  //    String query2 = "Create Slogan for ferrarri";
  //    when(openAIChat.run(query1)).thenReturn("ferrarri");
  //    when(openAIChat.run(query2)).thenReturn("We are the competition");
  //
  //    String template1 = "Write a creative name for a {{$area}} company.";
  //    String template2 = "Create Slogan for {{$company}}";
  //
  //    TemplateToTemplateLLMWrapperUnit promptTemplateToStringLLMUnit1 =
  //        TemplateToTemplateLLMWrapperUnit.create(openAIChat, Map.of("template", template2));
  //    TemplateToStringLLMWrapperUnit templateToStringLLMWrapperUnit2 =
  //        TemplateToStringLLMWrapperUnit.create(openAIChat);
  //
  //    PromptTemplateToPromptTemplateLLMNodeAdapter NodeAdapter1 =
  //        PromptTemplateToPromptTemplateLLMNodeAdapter.create(
  //            "company-name-gen", promptTemplateToStringLLMUnit1, List.of("slogan-gen"));
  //
  //    PromptTemplateToStringLLMNodeAdapter NodeAdapter2 =
  //        PromptTemplateToStringLLMNodeAdapter.create(
  //            "slogan-gen", templateToStringLLMWrapperUnit2, List.of());
  //
  //    CapabilityGraph CapabilityGraph = CapabilityGraph.create();
  //    CapabilityGraph.addNodeAdapter(NodeAdapter1, PromptTemplate.class);
  //    CapabilityGraph.addNodeAdapter(NodeAdapter2, PromptTemplate.class);
  //
  //    PromptTemplate promptTemplate1 =
  //        PromptTemplate.builder()
  //            .setTemplate("Write a creative name for a {{$area}} company.")
  //            .addVariableValuePair("area", "sport car")
  //            .build();
  //    // Act.
  //    Map<String, Object> result = CapabilityGraph.process(Map.of("company-name-gen",
  // promptTemplate1));
  //
  //    // Assert.
  //    assertThat(result.get("slogan-gen")).isEqualTo("We are the competition");
  //  }

  private static final class SumInputsProcessingUnit implements NodeAdapter<Integer, Integer> {
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

  private static final class MultiplyInputsProcessingUnit implements NodeAdapter<Integer, Integer> {
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

  private static final class NoOpProcessingUnit implements NodeAdapter<Integer, Integer> {
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
