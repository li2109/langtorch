package ai.knowly.langtoch.capability.dag;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.capability.unit.wrapper.TemplateToTemplateLLMWrapperUnit;
import ai.knowly.langtoch.capability.unit.wrapper.TemplateToStringLLMWrapperUnit;
import ai.knowly.langtoch.llm.providers.openai.OpenAIChat;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.collect.Streams;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CapabilityDAGTest {
  @Mock OpenAIChat openAIChat;

  @Test
  public void testProcessGraph_sumInputs() {
    // Arrange
    Node<Integer, Integer> a = new SumInputsProcessingUnit("A", Arrays.asList("B", "C"));
    Node<Integer, Integer> b = new SumInputsProcessingUnit("B", List.of("D"));
    Node<Integer, Integer> c = new SumInputsProcessingUnit("C", List.of("D"));
    Node<Integer, Integer> d = new SumInputsProcessingUnit("D", List.of());
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
    Node<Integer, Integer> b = new MultiplyInputsProcessingUnit("B", List.of("D"));
    Node<Integer, Integer> c = new MultiplyInputsProcessingUnit("C", List.of("D"));
    Node<Integer, Integer> d = new MultiplyInputsProcessingUnit("D", List.of());
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
    Node<Integer, Integer> a = new SumInputsProcessingUnit("A", List.of("B"));
    Node<Integer, Integer> b = new SumInputsProcessingUnit("B", List.of("C"));
    Node<Integer, Integer> c = new SumInputsProcessingUnit("C", List.of());
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
    Node<Integer, Integer> a = new NoOpProcessingUnit("A", List.of("B"));
    Node<Integer, Integer> b = new NoOpProcessingUnit("B", List.of("C"));
    Node<Integer, Integer> c = new NoOpProcessingUnit("C", List.of("A"));
    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(a, Integer.class);
    capabilityDAG.addNode(b, Integer.class);
    capabilityDAG.addNode(c, Integer.class);

    Map<String, Object> initialInputMap = new HashMap<>();
    initialInputMap.put("A", 1);

    // Act & Assert: Expect a RuntimeException due to the cycle in the graph
    assertThrows(RuntimeException.class, () -> capabilityDAG.process(initialInputMap));
  }

  @Test
  public void testConcatenatedNodes() {
    // Arrange.
    String query1 = "Write a creative name for a sport car company.";
    String query2 = "Create Slogan for ferrarri";
    when(openAIChat.run(query1)).thenReturn("ferrarri");
    when(openAIChat.run(query2)).thenReturn("We are the competition");

    String template1 = "Write a creative name for a {{$area}} company.";
    String template2 = "Create Slogan for {{$company}}";

    TemplateToTemplateLLMWrapperUnit promptTemplateToStringLLMUnit1 =
        new TemplateToTemplateLLMWrapperUnit(openAIChat, Map.of("template", template2));
    TemplateToStringLLMWrapperUnit templateToStringLLMWrapperUnit2 =
        new TemplateToStringLLMWrapperUnit(openAIChat);

    PromptTemplateToPromptTemplateLLMNode node1 =
        new PromptTemplateToPromptTemplateLLMNode(
            "company-name-gen", promptTemplateToStringLLMUnit1, List.of("slogan-gen"));

    PromptTemplateToStringLLMNode node2 =
        new PromptTemplateToStringLLMNode("slogan-gen", templateToStringLLMWrapperUnit2, List.of());

    CapabilityDAG capabilityDAG = new CapabilityDAG();
    capabilityDAG.addNode(node1, PromptTemplate.class);
    capabilityDAG.addNode(node2, PromptTemplate.class);

    PromptTemplate promptTemplate1 =
        PromptTemplate.builder()
            .setTemplate("Write a creative name for a {{$area}} company.")
            .addVariableValuePair("area", "sport car")
            .build();
    // Act.
    Map<String, Object> result = capabilityDAG.process(Map.of("company-name-gen", promptTemplate1));

    // Assert.
    assertThat(result.get("slogan-gen")).isEqualTo("We are the competition");
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
