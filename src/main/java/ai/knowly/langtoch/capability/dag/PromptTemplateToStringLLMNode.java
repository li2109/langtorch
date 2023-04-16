package ai.knowly.langtoch.capability.dag;

import ai.knowly.langtoch.capability.unit.TemplateToStringLLMWrapperUnit;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.collect.Iterables;
import java.util.List;

// This class represents a node in a graph that takes a PromptTemplate as input and returns a
// String.
public class PromptTemplateToStringLLMNode implements Node<PromptTemplate, String> {
  private final String id;
  private final List<String> outDegree;
  private final TemplateToStringLLMWrapperUnit unit;

  // Constructor for creating a new instance of the node.
  public PromptTemplateToStringLLMNode(
      String id, TemplateToStringLLMWrapperUnit unit, List<String> outDegree) {
    this.id = id;
    this.outDegree = outDegree;
    this.unit = unit;
  }

  // Returns the ID of the node.
  @Override
  public String getId() {
    return this.id;
  }

  // Returns the out-degree of the node.
  @Override
  public List<String> getOutDegree() {
    return this.outDegree;
  }

  // Processes the input PromptTemplate and returns the corresponding output String.
  @Override
  public String process(Iterable<PromptTemplate> inputs) {
    return unit.run(Iterables.getOnlyElement(inputs));
  }
}
