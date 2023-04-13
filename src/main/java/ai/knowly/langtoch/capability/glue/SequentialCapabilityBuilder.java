package ai.knowly.langtoch.capability.glue;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder class for creating sequential capability pipelines.
 *
 * @param <I> The input type for the first capability unit in the pipeline.
 * @param <O> The output type for the last capability unit in the pipeline.
 */
public class SequentialCapabilityBuilder<I, O> {
  private final List<CapabilityUnit<?, ?>> capabilityUnits = new ArrayList<>();

  /**
   * Creates a new SequentialCapabilityBuilder with the given first capability unit.
   *
   * @param firstUnit The first capability unit in the pipeline.
   */
  public SequentialCapabilityBuilder(CapabilityUnit<I, O> firstUnit) {
    capabilityUnits.add(firstUnit);
  }

  /**
   * Adds a new capability unit to the pipeline.
   *
   * @param nextUnit The next capability unit in the pipeline.
   * @param <T> The output type of the next capability unit.
   * @return A new SequentialCapabilityBuilder instance with the updated pipeline.
   */
  public <T> SequentialCapabilityBuilder<I, T> addUnit(CapabilityUnit<O, T> nextUnit) {
    capabilityUnits.add(nextUnit);
    return (SequentialCapabilityBuilder<I, T>) this;
  }

  /**
   * Builds the sequential capability pipeline and returns a functional interface.
   *
   * @return A CapabilityGlue instance that represents the sequential capability pipeline.
   */
  public CapabilityGlue<I, O> build() {
    return input -> {
      Object currentInput = input;

      for (CapabilityUnit<?, ?> unit : capabilityUnits) {
        currentInput = ((CapabilityUnit<Object, Object>) unit).run(currentInput);
      }

      return (O) currentInput;
    };
  }
}
