package ai.knowly.langtorch.tool;

import static com.google.common.util.concurrent.Futures.immediateFuture;

import ai.knowly.langtorch.agent.LLMTool;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

/** Search a 2D matrix for a target value. */
public class Search2DMatrixTool implements LLMTool<Search2DMatrixArgs, Boolean> {
  private static final Gson gson = new Gson();

  @Override
  public Search2DMatrixArgs preProcess(String inputData) {
    return gson.fromJson(inputData, Search2DMatrixArgs.class);
  }

  @Override
  public String postProcess(Boolean outputData) {
    return outputData.toString();
  }

  @Override
  public String getName() {
    return "Search2DMatrix";
  }

  @Override
  public String getDescription() {
    return "Search a 2D matrix for a target value.";
  }

  @Override
  public Boolean run(Search2DMatrixArgs inputData) {
    return searchMatrix(inputData.getMatrix(), inputData.getTarget());
  }

  @Override
  public ListenableFuture<Boolean> runAsync(Search2DMatrixArgs inputData) {
    return immediateFuture(run(inputData));
  }

  public boolean searchMatrix(int[][] matrix, int target) {
    int i = 0;
    int j = matrix[0].length - 1;
    while (i < matrix.length && j >= 0) {
      if (matrix[i][j] == target) return true;
      else if (matrix[i][j] > target) j--;
      else i++;
    }
    return false;
  }
}
