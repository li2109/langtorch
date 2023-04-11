package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.BaseStringInputParser;
import ai.knowly.langtoch.parser.output.BaseStringOutputParser;
import com.google.auto.value.AutoValue;
import java.util.Optional;

@AutoValue
public abstract class BaseLLMCapabilityUnit<T, R> extends BaseCapabilityUnit<T, R> {

  public static <T, R> Builder<T, R> builder() {
    return new AutoValue_BaseLLMCapabilityUnit.Builder<T, R>();
  }

  public abstract Optional<BaseModel> model();

  public abstract Optional<BaseStringOutputParser<R>> outputParser();

  public abstract Optional<BaseStringInputParser<T>> inputParser();

  @Override
  public R run(T input) {
    String parsedInput = inputParser().get().parse(input);
    String output = model().get().run(parsedInput);
    return outputParser().get().parse(output);
  }

  @AutoValue.Builder
  public abstract static class Builder<T, R> {

    public abstract Builder<T, R> setModel(BaseModel baseModel);

    public abstract Builder<T, R> setOutputParser(BaseStringOutputParser<R> parser);

    public abstract Builder<T, R> setInputParser(BaseStringInputParser<T> parser);

    abstract Optional<BaseStringOutputParser<R>> outputParser();

    abstract Optional<BaseStringInputParser<T>> inputParser();

    abstract Optional<BaseModel> model();

    abstract BaseLLMCapabilityUnit<T, R> autoBuild();

    public BaseLLMCapabilityUnit<T, R> build() {
      if (outputParser().isEmpty()) {
        throw new IllegalStateException("OutputParser must be set.");
      }
      if (inputParser().isEmpty()) {
        throw new IllegalStateException("InputParser must be set.");
      }
      if (model().isEmpty()) {
        throw new IllegalStateException("Model must be set.");
      }

      return autoBuild();
    }
  }
}
