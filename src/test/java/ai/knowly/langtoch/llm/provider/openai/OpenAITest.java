package ai.knowly.langtoch.llm.provider.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.ProcessorType;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessorRequestConverter;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessorConfig;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessorRequestConverter;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.chat.UserMessage;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenAITest {
  private static final OpenAIChatProcessorConfig openAIChatProcessorConfig =
      OpenAIChatProcessorConfig.builder().setModel("gpt-3.5-turbo").setMaxTokens(2048).build();
  private static final OpenAITextProcessorConfig OPEN_AI_STRING_PROCESSOR_CONFIG =
      OpenAITextProcessorConfig.builder().setModel("text-davinci-003").setMaxTokens(2048).build();
  @Mock private OpenAiService openAiService;

  @Test
  public void runWithTextProcessorTest() {
    // Arrange.
    OpenAITextProcessor openAITextProcessor = OpenAITextProcessor.create(openAiService);

    OpenAI openAI =
        OpenAI.create().withProcessor(ProcessorType.TEXT_PROCESSOR, openAITextProcessor);

    when(openAiService.createCompletion(
            OpenAITextProcessorRequestConverter.convert(OPEN_AI_STRING_PROCESSOR_CONFIG, "Hi!")))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                "What can i do for you?"));

    // Act.
    String response = openAI.runTextProcessor("Hi!");
    // Assert.
    assertThat(response).isEqualTo("What can i do for you?");
  }

  @Test
  public void runWithChatProcessorTest() {
    // Arrange.
    OpenAIChatProcessor openAIChatProcessor = OpenAIChatProcessor.create(openAiService);
    OpenAI openAI =
        OpenAI.create().withProcessor(ProcessorType.CHAT_PROCESSOR, openAIChatProcessor);

    when(openAiService.createChatCompletion(
            OpenAIChatProcessorRequestConverter.convert(
                openAIChatProcessorConfig,
                List.of(ai.knowly.langtoch.llm.schema.chat.ChatMessage.of(Role.USER, "Hi!")))))
        .thenReturn(
            OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ai.knowly.langtoch.llm.schema.chat.ChatMessage.of(
                    Role.ASSISTANT, "What can i do for you?")));

    // Act.
    ai.knowly.langtoch.llm.schema.chat.ChatMessage message =
        openAI.runChatProcessor(UserMessage.of("Hi!"));
    // Assert.
    assertThat(message.getMessage()).isEqualTo("What can i do for you?");
  }

  @Test
  public void runWithMultipleProcessorTest() {
    // Arrange.
    OpenAIChatProcessor openAIChatProcessor = OpenAIChatProcessor.create(openAiService);
    OpenAITextProcessor openAITextProcessor = OpenAITextProcessor.create(openAiService);
    OpenAI openAI =
        OpenAI.create()
            .withProcessor(ProcessorType.CHAT_PROCESSOR, openAIChatProcessor)
            .withProcessor(ProcessorType.TEXT_PROCESSOR, openAITextProcessor);

    when(openAiService.createChatCompletion(
            OpenAIChatProcessorRequestConverter.convert(
                openAIChatProcessorConfig,
                List.of(
                    ai.knowly.langtoch.llm.schema.chat.ChatMessage.of(
                        Role.USER, "Where is Changsha?")))))
        .thenReturn(
            OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ai.knowly.langtoch.llm.schema.chat.ChatMessage.of(
                    Role.ASSISTANT, "It's in hunan province, China.")));

    when(openAiService.createCompletion(
            OpenAITextProcessorRequestConverter.convert(OPEN_AI_STRING_PROCESSOR_CONFIG, "Hi!")))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                "What can i do for you?"));

    // Act.
    ai.knowly.langtoch.llm.schema.chat.ChatMessage message =
        openAI.runChatProcessor(UserMessage.of("Where is Changsha?"));
    String response = openAI.runTextProcessor("Hi!");
    // Assert.
    assertThat(message.getMessage()).isEqualTo("It's in hunan province, China.");
    assertThat(response).isEqualTo("What can i do for you?");
  }
}
