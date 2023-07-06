package ai.knowly.langtorch.tool;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.agent.Search2DMatrixArgs;
import ai.knowly.langtorch.agent.Tool;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

final class ToolTest {
  @Test
  void testTool_sum() {
    // Arrange.
    Function<List<Integer>, Integer> sum = integers -> integers.stream().mapToInt(i -> i).sum();

    Tool<List<Integer>, Integer> tool =
        Tool.<List<Integer>, Integer>builder()
            .setName("Calculator")
            .setDescription("The tool includes everything related to calculator.")
            .setFunction(sum)
            .build();

    // Act.
    int result = tool.invoke(ImmutableList.of(1, 2));
    // Assert.
    assertThat(result).isEqualTo(3);
  }

  // https://leetcode.com/problems/search-a-2d-matrix/
  @Test
  void testTool_search2DMatrix() {
    // Arrange.
    Function<Search2DMatrixArgs, Boolean> func =
        args -> searchMatrix(args.getMatrix(), args.getTarget());

    Tool<Search2DMatrixArgs, Boolean> tool =
        Tool.<Search2DMatrixArgs, Boolean>builder()
            .setName("LeetcodeSolver")
            .setDescription("Search a target in a 2D matrix.")
            .setFunction(func)
            .build();

    // Act.
    int[][] twoDArray = {
      {1, 3, 5, 7},
      {10, 11, 16, 20},
      {23, 30, 34, 60}
    };
    boolean result1 =
        tool.invoke(Search2DMatrixArgs.builder().setMatrix(twoDArray).setTarget(3).build());
    boolean result2 =
        tool.invoke(Search2DMatrixArgs.builder().setMatrix(twoDArray).setTarget(66).build());
    // Assert.
    assertThat(result1).isEqualTo(true);
    assertThat(result2).isEqualTo(false);
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
