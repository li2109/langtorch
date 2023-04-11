package ai.knowly.langtoch.capability.glue;

public abstract class BaseCapabilityGlue<T, R> {
  public abstract R run(T input);
}
