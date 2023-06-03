package ai.knowly.langtorch.llm.openai.schema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents the error body when an OpenAI request fails */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIError {

  private OpenAiErrorDetails error;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OpenAiErrorDetails {
    /** Human-readable error message */
    private String message;

    /**
     * OpenAI error type, for example "invalid_request_error"
     * https://platform.openai.com/docs/guides/error-codes/python-library-error-types
     */
    private String type;

    private String param;

    /** OpenAI error code, for example "invalid_api_key" */
    private String code;
  }
}
