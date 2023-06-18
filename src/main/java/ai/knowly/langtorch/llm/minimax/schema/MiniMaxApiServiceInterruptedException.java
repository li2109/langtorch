package ai.knowly.langtorch.llm.minimax.schema;

/**
 * @author maxiao
 * @date 2023/06/08
 */
public class MiniMaxApiServiceInterruptedException extends RuntimeException {
  public MiniMaxApiServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
