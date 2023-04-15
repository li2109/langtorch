package ai.knowly.langtoch.prompt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Prompt annotation is used to define a prompt template with variables. It contains a template
 * string, an optional list of variable names, and an optional name for the prompt.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Prompts.class)
public @interface Prompt {
  String template();

  String[] variables() default {};

  // The name of the prompt. This is only required when there are multiple Prompt annotations on a
  // single class.
  String name() default "";

  // The examples for the prompt. This is used for few-shot prompting.
  String[] examples() default {};

  // The header for the examples. Optional.
  String exampleHeader() default "";
}
