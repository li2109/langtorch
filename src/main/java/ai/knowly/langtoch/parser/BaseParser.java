package ai.knowly.langtoch.parser;

public abstract class BaseParser<T, R> {
  public abstract R parse(T input);
}