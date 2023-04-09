package ai.knowly.langtoch;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtoch.llm.openai.OpenAI;
import ai.knowly.langtoch.llm.openai.OpenAIChat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class test {

  @Test
  public void test() {
    // Arrange.
    OpenAI openAI = new OpenAI();
    // Act.
    String run = openAI.run("What is the synonym of Happy?");
    // Assert.
    assertThat(run).isEqualTo("Happy");
  }

  @Test
  public void test1() {
    // Arrange.
    OpenAIChat openAI = new OpenAIChat();
    // Act.
    String run = openAI.run("What is the synonym of Happy?");
    // Assert.
    assertThat(run).isEqualTo("Happy");
  }
}
