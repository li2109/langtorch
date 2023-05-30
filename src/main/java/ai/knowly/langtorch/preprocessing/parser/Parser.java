package ai.knowly.langtorch.preprocessing.parser;

@FunctionalInterface
public interface Parser<T, R> {
  R parse(T input);
}
