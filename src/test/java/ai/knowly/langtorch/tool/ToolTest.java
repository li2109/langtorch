package ai.knowly.langtorch.tool;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.agent.Tool;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

final class ToolTest {
  @Test
  void testTool_sum() {
    // Arrange.

    Tool<List<Integer>, Integer> tool = new IntegerListAdderTool();

    // Act.
    int result = tool.run(ImmutableList.of(1, 2));
    // Assert.
    assertThat(result).isEqualTo(3);
  }

  // https://leetcode.com/problems/search-a-2d-matrix/
  @Test
  void testTool_search2DMatrix() {
    // Arrange.
    Tool<Search2DMatrixArgs, Boolean> tool = new Search2DMatrixTool();

    // Act.
    int[][] twoDArray = {
      {1, 3, 5, 7},
      {10, 11, 16, 20},
      {23, 30, 34, 60}
    };
    boolean result1 = tool.run(new Search2DMatrixArgs().setMatrix(twoDArray).setTarget(3));
    boolean result2 = tool.run(new Search2DMatrixArgs().setMatrix(twoDArray).setTarget(66));
    // Assert.
    assertThat(result1).isEqualTo(true);
    assertThat(result2).isEqualTo(false);
  }
}
