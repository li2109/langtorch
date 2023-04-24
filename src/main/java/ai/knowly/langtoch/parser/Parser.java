package ai.knowly.langtoch.parser;

public abstract class Parser<T, R> {
  public abstract R parse(T input);
}
