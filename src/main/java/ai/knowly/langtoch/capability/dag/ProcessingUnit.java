package ai.knowly.langtoch.capability.dag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProcessingUnit {
  private final String id;
  private final List<String> outDegree;

  public ProcessingUnit(String id, List<String> outDegree) {
    this.id = id;
    this.outDegree = new ArrayList<>(outDegree);
  }

  public String getId() {
    return id;
  }

  public List<String> getOutDegree() {
    return Collections.unmodifiableList(outDegree);
  }

  public abstract Object process(Iterable<Object> inputs);
}
