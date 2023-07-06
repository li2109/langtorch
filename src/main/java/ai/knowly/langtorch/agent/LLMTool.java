package ai.knowly.langtorch.agent;

/** LLMTool is a tool that interact with the LLM via String. */
public interface LLMTool<T, R> extends Tool<T, R> {
  T preProcess(String inputData);

  String postProcess(R outputData);

  String getName();

  String getDescription();
}
