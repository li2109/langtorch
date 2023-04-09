package ai.knowly.langtoch.tool;

/** The common interface for all langtorch functions. */
public interface Function {
  Object execute(Object... args);
}
