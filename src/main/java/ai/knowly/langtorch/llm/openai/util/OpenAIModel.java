package ai.knowly.langtorch.llm.openai.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

// This is a Java enum class called `OpenAIModel` that defines a list of constants representing
// different OpenAI models. Each constant has a corresponding `String` value that represents the
// name
// of the model.
@Getter
@AllArgsConstructor
public enum OpenAIModel {
  GPT_3_5_TURBO("gpt-3.5-turbo"),
  GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
  GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
  GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613"),
  GPT_4("gpt-4"),
  GPT_4_32K("gpt-4-32k"),
  GPT_4_0613("gpt-4-0613"),
  GPT_4_32K_0613("gpt-4-32k-0613");
  private String value;
}
