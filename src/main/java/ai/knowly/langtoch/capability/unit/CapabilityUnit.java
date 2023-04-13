package ai.knowly.langtoch.capability.unit;

/**
 * An abstract class representing a capability unit in the pipeline.
 *
 * @param <T> The input type of the capability unit.
 * @param <R> The output type of the capability unit.
 */
public abstract class CapabilityUnit<T, R> {
  public abstract R run(T input);
}
