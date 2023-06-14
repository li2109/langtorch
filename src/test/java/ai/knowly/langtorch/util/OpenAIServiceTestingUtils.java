package ai.knowly.langtorch.util;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionChoice;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionChoice;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.Embedding;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.OpenAIKeyUtil;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class OpenAIServiceTestingUtils {
  public static final OpenAIService OPENAI_TESTING_SERVICE =
      OpenAIServiceProvider.createOpenAIService(OpenAIKeyUtil.getKey(Environment.TEST));

  // ChatCompletion related utils
  public static class ChatCompletion {
    public static ChatCompletionResult createChatCompletionResult(
        ai.knowly.langtorch.schema.chat.ChatMessage message) {
      return createChatCompletionResult(ImmutableList.of(message));
    }

    public static ChatCompletionResult createChatCompletionResult(
        ai.knowly.langtorch.schema.chat.ChatMessage... messages) {
      return createChatCompletionResult(ImmutableList.copyOf(messages));
    }

    public static ChatCompletionResult createChatCompletionResult(
        Iterable<ai.knowly.langtorch.schema.chat.ChatMessage> chatMessages) {
      ChatCompletionResult completionResult = new ChatCompletionResult();
      completionResult.setChoices(createChatCompletionChoices(chatMessages));
      return completionResult;
    }

    public static ImmutableList<ChatCompletionChoice> createChatCompletionChoices(
        ai.knowly.langtorch.schema.chat.ChatMessage... chatMessages) {
      return createChatCompletionChoices(ImmutableList.copyOf(chatMessages));
    }

    public static ImmutableList<ChatCompletionChoice> createChatCompletionChoices(
        Iterable<ai.knowly.langtorch.schema.chat.ChatMessage> chatMessages) {
      ImmutableList.Builder<ChatCompletionChoice> builder = ImmutableList.builder();
      for (ai.knowly.langtorch.schema.chat.ChatMessage chatMessage : chatMessages) {
        ChatCompletionChoice completionChoice = new ChatCompletionChoice();
        completionChoice.setMessage(chatMessage);
        builder.add(completionChoice);
      }
      return builder.build();
    }
  }

  // TextCompletion related utils
  public static class TextCompletion {
    public static CompletionResult createCompletionResult(String text) {
      return createCompletionResult(ImmutableList.of(text));
    }

    public static CompletionResult createCompletionResult(String... texts) {
      return createCompletionResult(ImmutableList.copyOf(texts));
    }

    public static CompletionResult createCompletionResult(Iterable<String> texts) {
      CompletionResult completionResult = new CompletionResult();
      completionResult.setChoices(createCompletionChoices(texts));
      return completionResult;
    }

    public static ImmutableList<CompletionChoice> createCompletionChoices(String... texts) {
      return createCompletionChoices(ImmutableList.copyOf(texts));
    }

    public static ImmutableList<CompletionChoice> createCompletionChoices(Iterable<String> texts) {
      ImmutableList.Builder<CompletionChoice> builder = ImmutableList.builder();
      for (String text : texts) {
        CompletionChoice completionChoice = new CompletionChoice();
        completionChoice.setText(text);
        builder.add(completionChoice);
      }
      return builder.build();
    }
  }

  public static class Embeddings {
    public static EmbeddingResult createEmbeddingResult() {
      EmbeddingResult embeddingResult = new EmbeddingResult();
      embeddingResult.setObject("list");
      List<Embedding> embeddings = new ArrayList<>();
      embeddingResult.setData(embeddings);
      return embeddingResult;
    }
  }
}
