//package ai.knowly.langtoch.capability.unit;
//
//import ai.knowly.langtoch.llm.provider.LargeLanguageModelProvider;
//import ai.knowly.langtoch.parser.input.StringInputParser;
//import ai.knowly.langtoch.parser.output.StringOutputParser;
//import com.google.auto.value.AutoValue;
//import java.util.Optional;
//
///**
// * An abstract class representing a Large Language Model Capability Unit.
// *
// * @param <T> The input type of the capability unit.
// * @param <R> The output type of the capability unit.
// */
//@AutoValue
//public abstract class LLMCapabilityUnit<T, R> extends CapabilityUnit<T, R> {
//
//  public static <T, R> Builder<T, R> builder() {
//    return new AutoValue_LLMCapabilityUnit.Builder<T, R>();
//  }
//
//  public abstract Optional<LargeLanguageModelProvider> provider();
//
//  public abstract Optional<StringOutputParser<R>> outputParser();
//
//  public abstract Optional<StringInputParser<T>> inputParser();
//
//  @Override
//  public R run(T input) {
//    String parsedInput =
//        inputParser().isPresent() ? inputParser().get().parse(input) : (String) input;
//    String output = provider().get().run(parsedInput);
//    return outputParser().isPresent() ? outputParser().get().parse(output) : (R) output;
//  }
//
//  /**
//   * Builder class for LLMCapabilityUnit.
//   *
//   * @param <T> The input type of the capability unit.
//   * @param <R> The output type of the capability unit.
//   */
//  @AutoValue.Builder
//  public abstract static class Builder<T, R> {
//    public abstract Builder<T, R> setProvider(LargeLanguageModelProvider processor);
//
//    public abstract Builder<T, R> setOutputParser(StringOutputParser<R> parser);
//
//    public abstract Builder<T, R> setInputParser(StringInputParser<T> parser);
//
//    abstract Optional<StringOutputParser<R>> outputParser();
//
//    abstract Optional<StringInputParser<T>> inputParser();
//
//    abstract Optional<LargeLanguageModelProvider> provider();
//
//    abstract LLMCapabilityUnit<T, R> autoBuild();
//
//    public LLMCapabilityUnit<T, R> build() {
//      if (provider().isEmpty()) {
//        throw new IllegalStateException("Large language model provider must be set.");
//      }
//
//      return autoBuild();
//    }
//  }
//}
