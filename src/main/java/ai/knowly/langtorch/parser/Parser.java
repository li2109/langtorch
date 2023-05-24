package ai.knowly.langtorch.parser;

@FunctionalInterface
public interface Parser<T, R> {
  R parse(T input);
}
