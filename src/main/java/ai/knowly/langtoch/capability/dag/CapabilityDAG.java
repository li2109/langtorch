package ai.knowly.langtoch.capability.dag;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class CapabilityDAG {
  private final HashMap<String, ProcessingUnit> processingUnits = new HashMap<>();
  private final Multimap<String, Object> inputMap = ArrayListMultimap.create();
  private final HashMap<String, Object> outputMap = new HashMap<>();
  private final Multimap<String, String> inDegreeMap = ArrayListMultimap.create();

  public CapabilityDAG(List<ProcessingUnit> units) {
    for (ProcessingUnit unit : units) {
      addUnit(unit);
    }
  }

  public Map<String, Object> process(Map<String, Object> initialInputMap) {
    for (Map.Entry<String, Object> entry : initialInputMap.entrySet()) {
      setInitialInput(entry.getKey(), entry.getValue());
    }
    List<String> sortedList = topologicalSort();

    for (String id : sortedList) {
      ProcessingUnit unit = processingUnits.get(id);
      Collection<Object> input = inputMap.get(id);
      Object output = unit.process(input);
      addOutput(id, output);
      for (String outDegree : unit.getOutDegree()) {
        addInput(outDegree, output);
      }
    }

    Map<String, Object> result = new HashMap<>();
    for (String id : getEndNodeIds()) {
      result.put(id, outputMap.get(id));
    }
    return result;
  }

  public Object getOutput(String id) {
    return outputMap.get(id);
  }

  private List<String> getEndNodeIds() {
    List<String> endNodeIds = new ArrayList<>();
    for (ProcessingUnit unit : processingUnits.values()) {
      if (unit.getOutDegree().isEmpty()) {
        endNodeIds.add(unit.getId());
      }
    }
    return endNodeIds;
  }

  private void addUnit(ProcessingUnit unit) {
    processingUnits.put(unit.getId(), unit);
    for (String outDegree : unit.getOutDegree()) {
      inDegreeMap.put(outDegree, unit.getId());
    }
  }

  private void setInitialInput(String id, Object input) {
    inputMap.put(id, input);
  }

  private void addOutput(String id, Object output) {
    outputMap.put(id, output);
  }

  private void addInput(String id, Object input) {
    inputMap.put(id, input);
  }

  private List<String> topologicalSort() {
    List<String> sortedList = new ArrayList<>();
    Queue<String> queue = new LinkedList<>();

    HashMap<String, Integer> inDegreeCount = new HashMap<>();
    for (String node : processingUnits.keySet()) {
      inDegreeCount.put(node, inDegreeMap.get(node).size());
      if (inDegreeCount.get(node) == 0) {
        queue.add(node);
      }
    }

    while (!queue.isEmpty()) {
      String current = queue.poll();
      sortedList.add(current);

      for (String neighbor : processingUnits.get(current).getOutDegree()) {
        int updatedInDegree = inDegreeCount.get(neighbor) - 1;
        inDegreeCount.put(neighbor, updatedInDegree);

        if (updatedInDegree == 0) {
          queue.add(neighbor);
        }
      }
    }

    if (sortedList.size() != processingUnits.size()) {
      throw new RuntimeException("The graph contains a cycle and cannot be topologically sorted.");
    }

    return sortedList;
  }
}
