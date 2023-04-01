package ai.knowly.llm.openai;

import static com.google.common.truth.Truth.assertThat;

import com.google.acai.Acai;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OpenAIModelTest {
  @Rule public Acai acai = new Acai(MyTestModule.class);

  @Inject OpenAI openAI;

  @Test
  public void testSetUpIsUpAndRunning() {
    // Arrange.
    OpenAI model = openAI.setModel("ada").setTemperature(0.9);
    // Act.
    String response = model.run("Who is Lady Gaga and how old is she?");
    System.out.println(response);
    // Assert.
    assertThat(response).isNotEmpty();
  }

  private static class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      install(new OpenAIServiceModule());
    }
  }
}
