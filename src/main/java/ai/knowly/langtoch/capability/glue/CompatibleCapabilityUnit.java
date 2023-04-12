package ai.knowly.langtoch.capability.glue;

import ai.knowly.langtoch.capability.unit.BaseCapabilityUnit;

public class CompatibleCapabilityUnit<I, O, R> {
  private final BaseCapabilityUnit<I, O> first;
  private final BaseCapabilityUnit<O, R> second;

  public CompatibleCapabilityUnit(BaseCapabilityUnit<I, O> first, BaseCapabilityUnit<O, R> second) {
    this.first = first;
    this.second = second;
  }

  public R run(Object input) {
    @SuppressWarnings("unchecked")
    I castedInput = (I) input;

    O intermediateResult = first.run(castedInput);
    if (second == null) {
      return (R) intermediateResult;
    }
    return second.run(intermediateResult);
  }

  public BaseCapabilityUnit<I, O> getFirst() {
    return first;
  }

  public BaseCapabilityUnit<O, R> getSecond() {
    return second;
  }
}
