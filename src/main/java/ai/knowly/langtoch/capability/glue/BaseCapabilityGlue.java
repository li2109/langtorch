package ai.knowly.langtoch.capability.glue;

public abstract class BaseCapabilityGlue<I, O> {
  public abstract O run(I input);
}
