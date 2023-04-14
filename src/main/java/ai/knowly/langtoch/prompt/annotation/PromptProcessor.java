package ai.knowly.langtoch.prompt.annotation;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PromptProcessor {

  /**
   * Creates a PromptTemplate using the given annotated class and its instance.
   *
   * @param clazz the annotated class
   * @param instance the instance of the annotated class
   * @return a PromptTemplate with the template and variables extracted from the annotated class
   */
  public static PromptTemplate createPromptTemplate(Class<?> clazz, Object instance) {
    validateAnnotatedClass(clazz);

    Prompt promptAnnotation = clazz.getAnnotation(Prompt.class);
    String template = promptAnnotation.template();
    String[] variableNames = promptAnnotation.variables();

    Map<String, String> variableValues = extractVariableValues(clazz, instance, variableNames);

    return PromptTemplate.builder()
        .setTemplate(template)
        .addAllVariableValuePairs(variableValues)
        .build();
  }

  /**
   * Validates if the given class is annotated with the @Prompt annotation.
   *
   * @param clazz the class to be validated
   */
  private static void validateAnnotatedClass(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(Prompt.class)) {
      throw new IllegalArgumentException("Class should be annotated with @Prompt");
    }
  }

  /**
   * Extracts the variable values from the given class and instance.
   *
   * @param clazz the annotated class
   * @param instance the instance of the annotated class
   * @param variableNames an array of variable names
   * @return a Map containing variable names and their corresponding values
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
