package ai.knowly.langtorch.llm.minimax.schema;
/**
 * @author maxiao
 * @date 2023/06/17
 */
public class MiniMaxApiBusinessErrorException extends RuntimeException {

  private final Long statusCode;

  public MiniMaxApiBusinessErrorException(Long statusCode, String statusMessage) {
    super(statusMessage);
    this.statusCode = statusCode;
  }
}
