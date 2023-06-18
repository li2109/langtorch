package ai.knowly.langtorch.llm.minimax.schema.dto;

import lombok.Data;

/**
 * @author maxiao
 * @date 2023/06/17
 */
@Data
public class BaseResp {

  /**
   * Status code 1000, unknown error 1001, timeout 1002, triggering current limit 1004,
   * authentication failure 1008, balance less than 1013, internal service error 1027, serious
   * violation of output content 2013, abnormal input format information
   */
  private Long statusCode;

  /** Error details */
  private String statusMsg;
}
