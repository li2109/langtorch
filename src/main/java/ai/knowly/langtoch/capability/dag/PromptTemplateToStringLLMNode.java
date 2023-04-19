package ai.knowly.langtoch.capability.dag;

import ai.knowly.langtoch.capability.unit.wrapper.TemplateToStringLLMWrapperUnit;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Iterables;
import java.util.List;

/**
 * This class represents a node in a graph that takes a PromptTemplate as input and returns a
 * String.
 */
@AutoValue
public abstract class PromptTemplateToStringLLMNode implements Node<PromptTemplate, String> {

  public static PromptTemplateToStringLLMNode create(
      String id, TemplateToStringLLMWrapperUnit unit, List<String> outDegree) {
    return new AutoValue_PromptTemplateToStringLLMNode(id, outDegree, unit);
  }

  abstract String id();

  abstract List<String> outDegree();

  abstract TemplateToStringLLMWrapperUnit unit();

  // Returns the ID of the node.
  @Override
  public String getId() {
    return id();
  }

  // Returns the out-degree of the node.
  @Override
  public List<String> getOutDegree() {
    return outDegree();
  }

  // Processes the input PromptTemplate and returns the corresponding output String.
  @Override
  public String process(Iterable<PromptTemplate> inputs) {
    return unit().run(Iterables.getOnlyElement(inputs));
  }
}
