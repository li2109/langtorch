package ai.knowly.langtoch.capability.glue;

import ai.knowly.langtoch.capability.unit.BaseCapabilityUnit;
import java.util.ArrayList;
import java.util.List;

public class SequentialCapabilityBuilder<I, O> {
  private final List<BaseCapabilityUnit<?, ?>> compatibleUnits = new ArrayList<>();

  public SequentialCapabilityBuilder(BaseCapabilityUnit<I, O> firstUnit) {
    compatibleUnits.add(firstUnit);
  }

  public <T> SequentialCapabilityBuilder<I, T> addUnit(BaseCapabilityUnit<O, T> nextUnit) {
    compatibleUnits.add(nextUnit);
    return (SequentialCapabilityBuilder<I, T>) this;
  }

  public BaseCapabilityGlue<I, O> build() {
    return new BaseCapabilityGlue<I, O>() {
      @Override
      public O run(I input) {
        Object currentInput = input;

        for (BaseCapabilityUnit<?, ?> unit : compatibleUnits) {
          currentInput = ((BaseCapabilityUnit<Object, Object>) unit).run(currentInput);
        }

        return (O) currentInput;
      }
    };
  }
}
