package ai.knowly.langtoch.llm.provider.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.ProcessorType;
import ai.knowly.langtoch.llm.processor.openai.chat.ChatProcessor;
import ai.knowly.langtoch.llm.processor.openai.chat.ChatProcessorConfig;
import ai.knowly.langtoch.llm.processor.openai.chat.ChatProcessorRequestConverter;
import ai.knowly.langtoch.llm.processor.openai.text.TextProcessor;
import ai.knowly.langtoch.llm.processor.openai.text.TextProcessorConfig;
import ai.knowly.langtoch.llm.processor.openai.text.TextProcessorRequestConverter;
import ai.knowly.langtoch.llm.schema.chat.Role;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenAITest {
  @Mock private OpenAiService openAiService;

  @Test
  public void runWithTextProcessorTest() {
    // Arrange.
    TextProcessor textProcessor = TextProcessor.create(openAiService);
    OpenAI openAI = OpenAI.create().withProcessor(ProcessorType.TEXT_PROCESSOR, textProcessor);
    CompletionRequest completionRequest =
        TextProcessorRequestConverter.convert(TextProcessorConfig.builder().build(), "Hi!");

    CompletionResult completionResult = new CompletionResult();
    CompletionChoice completionChoice = new CompletionChoice();
    completionChoice.setText("What can i do for you?");
    completionResult.setChoices(Arrays.asList(completionChoice));

    when(openAiService.createCompletion(completionRequest)).thenReturn(completionResult);

    // Act.
    String response = openAI.run("Hi!");
    // Assert.
    assertThat(response).isEqualTo("What can i do for you?");
  }

  @Test
  public void runWithChatProcessorTest() {
    // Arrange.
    ChatProcessor chatProcessor = ChatProcessor.create(openAiService);
    OpenAI openAI = OpenAI.create().withProcessor(ProcessorType.CHAT_PROCESSOR, chatProcessor);
    ChatCompletionRequest completionRequest =
        ChatProcessorRequestConverter.convert(
            ChatProcessorConfig.builder().build(),
            List.of(ai.knowly.langtoch.llm.schema.chat.ChatMessage.of(Role.USER, "Hi!")));

    ChatCompletionResult completionResult = new ChatCompletionResult();

    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setContent("What can i do for you?");
    chatMessage.setRole("assistant");

    ChatCompletionChoice completionChoice = new ChatCompletionChoice();
    completionChoice.setMessage(chatMessage);
    completionResult.setChoices(Arrays.asList(completionChoice));

    when(openAiService.createChatCompletion(completionRequest)).thenReturn(completionResult);

    // Act.
    String response = openAI.run("Hi!");
    // Assert.
    assertThat(response).isEqualTo("What can i do for you?");
  }
}
