package ai.knowly.langtorch.llm.cohere.schema;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CohereError {
  public static Builder builder() {
    return new AutoValue_CohereError.Builder();
  }

  public abstract Integer code();

  public abstract String message();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setCode(Integer code);

    public abstract Builder setMessage(String message);

    public abstract CohereError build();
  }
}
