package ai.knowly.llm.openai;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.llm.message.BaseChatMessage;
import ai.knowly.llm.message.SystemMessage;
import ai.knowly.llm.message.UserMessage;
import ai.knowly.llm.openai.OpenAIChat;
import ai.knowly.llm.openai.OpenAIServiceModule;
import com.google.acai.Acai;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OpenAIChatModelTest {
  @Rule public Acai acai = new Acai(MyTestModule.class);

  @Inject OpenAIChat openAIChat;

  @Test
  public void testMessageWithCompleteSetup() {
    // Arrange.
    OpenAIChat model = openAIChat.setModel("gpt-3.5-turbo").setTemperature(0);
    // Act.
    BaseChatMessage message =
        model.run(
            ImmutableList.of(
                SystemMessage.builder()
                    .setMessage(
                        "You are an excellent mathematician. Provide a detailed thought process step by step. ")
                    .build(),
                UserMessage.builder().setMessage("What's the result of 32 * 100?").build()));
    System.out.println(message);
    // Assert.
    assertThat(message.returnMessage()).isNotEmpty();
  }

  @Test
  public void testMessageWithMinimalSetup() {
    // Arrange.
    OpenAIChat model = openAIChat.setModel("gpt-3.5-turbo").setTemperature(0);
    // Act.
    String message = model.run("What's the result of 32 * 100?");
    System.out.println(message);
    // Assert.
    assertThat(message).isNotEmpty();
  }

  private static class MyTestModule extends AbstractModule {
    @Override
    protected void configure() {
      install(new OpenAIServiceModule());
    }
  }
}
