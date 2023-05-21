package ai.knowly.langtorch.capability.usecase.doc;

import ai.knowly.langtorch.capability.Capability;
import com.google.common.util.concurrent.ListenableFuture;

public class DocEmbeddingLLMCapability implements Capability<String, String> {

  @Override
  public String run(String inputData) {
    return null;
  }

  @Override
  public ListenableFuture<String> runAsync(String inputData) {
    return null;
  }
}
