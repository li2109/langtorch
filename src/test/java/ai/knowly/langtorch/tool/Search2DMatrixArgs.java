package ai.knowly.langtorch.tool;

import com.google.auto.value.AutoBuilder;

public class Search2DMatrixArgs {
  private int[][] matrix;
  private int target;

  public int[][] getMatrix() {
    return matrix;
  }

  public Search2DMatrixArgs setMatrix(int[][] matrix) {
    this.matrix = matrix;
    return this;
  }

  public int getTarget() {
    return target;
  }

  public Search2DMatrixArgs setTarget(int target) {
    this.target = target;
    return this;
  }
}
