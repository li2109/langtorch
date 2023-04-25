package ai.knowly.langtoch.parser;

@FunctionalInterface
public interface Parser<T, R> {
  R parse(T input);
}
