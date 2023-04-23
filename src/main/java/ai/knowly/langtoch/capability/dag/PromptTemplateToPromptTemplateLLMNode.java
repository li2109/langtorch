//package ai.knowly.langtoch.capability.dag;
//
//import ai.knowly.langtoch.capability.unit.wrapper.TemplateToTemplateLLMWrapperUnit;
//import ai.knowly.langtoch.prompt.template.PromptTemplate;
//import com.google.auto.value.AutoValue;
//import com.google.common.collect.Iterables;
//import java.util.List;
//
///**
// * This class represents a node in a graph that takes a PromptTemplate as input and returns another
// * PromptTemplate.
// */
//@AutoValue
//public abstract class PromptTemplateToPromptTemplateLLMNode
//    implements Node<PromptTemplate, PromptTemplate> {
//
//  public static PromptTemplateToPromptTemplateLLMNode create(
//      String id, TemplateToTemplateLLMWrapperUnit unit, List<String> outDegree) {
//    return new AutoValue_PromptTemplateToPromptTemplateLLMNode(id, outDegree, unit);
//  }
//
//  abstract String id();
//
//  abstract List<String> outDegree();
//
//  abstract TemplateToTemplateLLMWrapperUnit unit();
//
//  // Returns the ID of the node.
//  @Override
//  public String getId() {
//    return id();
//  }
//
//  // Returns the out-degree of the node.
//  @Override
//  public List<String> getOutDegree() {
//    return outDegree();
//  }
//
//  // Processes the input PromptTemplate and returns the corresponding output PromptTemplate.
//  @Override
//  public PromptTemplate process(Iterable<PromptTemplate> inputs) {
//    return unit().run(Iterables.getOnlyElement(inputs));
//  }
//}
