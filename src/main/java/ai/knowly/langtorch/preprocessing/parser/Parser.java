package ai.knowly.langtorch.preprocessing.parser;

// This code defines a functional interface named `Parser` with two generic type parameters `T` and
// `R`. It has a single abstract method `parse` that takes an input of type `T` and returns a result
// of
// type `R`.
@FunctionalInterface
public interface Parser<T, R> {
  R parse(T input);
}
