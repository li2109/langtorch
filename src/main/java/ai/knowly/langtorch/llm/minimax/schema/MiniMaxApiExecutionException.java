package ai.knowly.langtorch.llm.minimax.schema;

/**
 * @author maxiao
 * @date 2023/06/07
 */
public class MiniMaxApiExecutionException extends RuntimeException {
  public MiniMaxApiExecutionException(Exception e) {
    super(e);
  }
}
