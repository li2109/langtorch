package ai.knowly.langtoch.llm.base;

/** A model that takes in a prompt and returns a response. */
public abstract class BaseModel {
  public abstract String run(String prompt);
}
