package ai.knowly.langtoch.tool;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ToolTest {
  @Test
  public void testTool_sum() {
    // Arrange.
    Function sum =
        args -> {
          int a = (int) args[0];
          int b = (int) args[1];
          return a + b;
        };

    Tool tool =
        Tool.builder()
            .setName("Calculator")
            .setDescription("The tool includes everything related to calculator.")
            .register("add", sum)
            .build();

    // Act.
    int result = (int) tool.invoke("add", 1, 2);
    // Assert.
    assertThat(result).isEqualTo(3);
  }

  // https://leetcode.com/problems/search-a-2d-matrix/
  @Test
  public void testTool_search2DMatrix() {
    // Arrange.
    Function func = args -> searchMatrix((int[][]) args[0], (int) args[1]);

    Tool tool =
        Tool.builder()
            .setName("LeetcodeSolver")
            .setDescription("Search a target in a 2D matrix.")
            .register("search_2d_matrix", func)
            .build();

    // Act.
    int[][] twoDArray = {
      {1, 3, 5, 7},
      {10, 11, 16, 20},
      {23, 30, 34, 60}
    };
    boolean result1 = (boolean) tool.invoke("search_2d_matrix", twoDArray, 3);
    boolean result2 = (boolean) tool.invoke("search_2d_matrix", twoDArray, 66);
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
