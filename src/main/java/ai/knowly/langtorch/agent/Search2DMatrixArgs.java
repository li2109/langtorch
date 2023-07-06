package ai.knowly.langtorch.agent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class Search2DMatrixArgs {
  private int[][] matrix;
  private int target;
}
