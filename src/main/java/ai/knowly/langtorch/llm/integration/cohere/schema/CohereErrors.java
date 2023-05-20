package ai.knowly.langtorch.llm.integration.cohere.schema;

public class CohereErrors {
  public static final CohereError COHERE_ERROR_400 =
      CohereError.builder().setCode(400).setMessage("Bad Request").build();
  public static final CohereError COHERE_ERROR_498 =
      CohereError.builder().setCode(498).setMessage("Blocked Input or Output").build();
  public static final CohereError COHERE_ERROR_500 =
      CohereError.builder().setCode(500).setMessage("Internal Server Error").build();
}
