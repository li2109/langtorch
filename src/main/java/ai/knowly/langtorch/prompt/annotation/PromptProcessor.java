package ai.knowly.langtorch.prompt.annotation;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The PromptProcessor is responsible for processing Prompt and Prompts annotations on a class. It
 * can create a PromptTemplate based on the annotations and the fields of the annotated class.
 */
public class PromptProcessor {
  /**
   * Create a PromptTemplate using the single Prompt annotation on the class. This method should be
   * used when there is only one Prompt annotation on the class.
   *
   * @param clazz The annotated class.
   * @param instance An instance of the annotated class.
   * @return A PromptTemplate based on the annotation and the fields of the class.
   */
  public static PromptTemplate createPromptTemplate(Class<?> clazz, Object instance) {
    validateAnnotatedClass(clazz);

    if (clazz.isAnnotationPresent(Prompts.class)
        || clazz.getAnnotationsByType(Prompt.class).length > 1) {
      throw new IllegalArgumentException(
          "Ambiguous prompt annotations. Please specify a prompt name.");
    }

    Prompt promptAnnotation = clazz.getAnnotation(Prompt.class);
    return createPromptTemplateFromClassAndInstance(clazz, instance, promptAnnotation);
  }

  /**
   * Create a PromptTemplate using a specific Prompt annotation on the class. This method should be
   * used when there are multiple Prompt annotations on the class.
   *
   * @param clazz The annotated class.
   * @param instance An instance of the annotated class.
   * @param promptName The name of the Prompt annotation to use.
   * @return A PromptTemplate based on the specified annotation and the fields of the class.
   */
  public static PromptTemplate createPromptTemplate(
      Class<?> clazz, Object instance, String promptName) {
    validateAnnotatedClass(clazz);

    Prompt[] prompts = getPrompts(clazz);
    Prompt promptAnnotation = findPromptByName(promptName, prompts);
    return createPromptTemplateFromClassAndInstance(clazz, instance, promptAnnotation);
  }

  private static PromptTemplate createPromptTemplateFromClassAndInstance(
      Class<?> clazz, Object instance, Prompt promptAnnotation) {
    String template = promptAnnotation.template();
    String[] variableNames = promptAnnotation.variables();
    String[] examples = promptAnnotation.examples();
    String exampleHeader = promptAnnotation.exampleHeader();

    Map<String, String> variableValues = extractVariableValues(clazz, instance, variableNames);

    PromptTemplate.Builder builder =
        PromptTemplate.builder().setTemplate(template).addAllVariableValuePairs(variableValues);

    if (examples.length > 0) {
      builder.setExamples(List.of(examples));
      if (!exampleHeader.isEmpty()) {
        builder.setExampleHeader(exampleHeader);
      }
    }

    return builder.build();
  }

  /**
   * Validates that the class has either a Prompt or Prompts annotation.
   *
   * @param clazz The class to validate.
   */
  private static void validateAnnotatedClass(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(Prompt.class) && !clazz.isAnnotationPresent(Prompts.class)) {
      throw new IllegalArgumentException("Class should be annotated with @Prompt or @Prompts");
    }
  }

  /**
   * Retrieves an array of Prompt annotations from the class. If the class has a Prompts annotation,
   * it returns the array of Prompt annotations from it. If the class has a single Prompt
   * annotation, it returns an array containing that annotation.
   *
   * @param clazz The class to get the Prompt annotations from.
   * @return An array of Prompt annotations.
   */
  private static Prompt[] getPrompts(Class<?> clazz) {
    if (clazz.isAnnotationPresent(Prompts.class)) {
      return clazz.getAnnotation(Prompts.class).value();
    } else {
      return new Prompt[] {clazz.getAnnotation(Prompt.class)};
    }
  }

  /**
   * Finds a Prompt annotation with the specified name in the array of Prompt annotations.
   *
   * @param promptName The name of the Prompt annotation to find.
   * @param prompts The array of Prompt annotations to search in.
   * @return The found Prompt annotation.
   * @throws IllegalArgumentException if no Prompt annotation with the specified name is found.
   */
  private static Prompt findPromptByName(String promptName, Prompt[] prompts) {
    for (Prompt prompt : prompts) {
      if (prompt.name().equals(promptName)) {
        return prompt;
      }
    }
    throw new IllegalArgumentException("No prompt found with the specified name.");
  }

  /**
   * Extracts variable values from the fields of the class based on the variable names provided.
   *
   * @param clazz The class containing the fields.
   * @param instance An instance of the class.
   * @param variableNames An array of variable names to extract values for.
   * @return A map containing variable names and their corresponding values.
   * @throws IllegalArgumentException if a field with the specified name is not found or is
   *     inaccessible.
   */
  private static Map<String, String> extractVariableValues(
      Class<?> clazz, Object instance, String[] variableNames) {
    Map<String, String> variableValues = new HashMap<>();
    for (String variableName : variableNames) {
      try {
        Field field = clazz.getDeclaredField(variableName);
        field.setAccessible(true);
        String fieldValue = (String) field.get(instance);
        variableValues.put(variableName, fieldValue);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new IllegalArgumentException("Unable to extract variable value", e);
      }
    }
    return variableValues;
  }
}
