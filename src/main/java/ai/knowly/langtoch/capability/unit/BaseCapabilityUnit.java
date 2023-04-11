package ai.knowly.langtoch.capability.unit;

/**
 * Base class for all capability units that take in input with type T and return output with type R.
 */
public abstract class BaseCapabilityUnit<T, R> {
  public abstract R run(T input);
}
