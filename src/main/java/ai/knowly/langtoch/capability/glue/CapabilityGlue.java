package ai.knowly.langtoch.capability.glue;

/**
 * A functional interface that represents a capability glue.
 *
 * @param <T> The input type.
 * @param <R> The output type.
 */
@FunctionalInterface
public interface CapabilityGlue<T, R> {
  R run(T input);
}
