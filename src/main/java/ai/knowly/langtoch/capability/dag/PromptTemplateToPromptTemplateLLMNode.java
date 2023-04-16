package ai.knowly.langtoch.capability.dag;

import ai.knowly.langtoch.capability.unit.TemplateToTemplateLLMWrapperUnit;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.collect.Iterables;
import java.util.List;

// This class represents a node in a graph that takes a PromptTemplate as input and returns another
// PromptTemplate.
public class PromptTemplateToPromptTemplateLLMNode implements Node<PromptTemplate, PromptTemplate> {
  private final String id;
  private final List<String> outDegree;
  private final TemplateToTemplateLLMWrapperUnit unit;

  // Constructor for creating a new instance of the node.
  public PromptTemplateToPromptTemplateLLMNode(
      String id, TemplateToTemplateLLMWrapperUnit unit, List<String> outDegree) {
    this.unit = unit;
    this.id = id;
    this.outDegree = outDegree;
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

  // Processes the input PromptTemplate and returns the corresponding output PromptTemplate.
  @Override
  public PromptTemplate process(Iterable<PromptTemplate> inputs) {
    return unit.run(Iterables.getOnlyElement(inputs));
  }
}
