package ai.knowly.langtorch.processor.cohere.generate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.cohere.CohereApiService;
import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateResponse;
import ai.knowly.langtorch.llm.cohere.schema.Generation;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class CohereGenerateProcessorTest {

  @Mock @Bind private CohereApiService CohereApiService;

  @Bind
  private CohereGenerateProcessorConfig config = CohereGenerateProcessorConfig.builder().build();

  @Inject private CohereGenerateProcessor cohereGenerateProcessor;

  @BeforeEach
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  void run() {
    // Arrange.
    Generation generation = new Generation();
    generation.setText("This ice cream is sweet and delicious");
    CohereGenerateResponse generateResponse = new CohereGenerateResponse();
    generateResponse.setGenerations(ImmutableList.of(generation));
    when(CohereApiService.generate(any())).thenReturn(generateResponse);

    // Act.
    SingleText actualOutput = cohereGenerateProcessor.run(SingleText.of("ice cream"));

    // Assert.
    assertEquals("This ice cream is sweet and delicious", actualOutput.getText());
  }

  @Test
  void runAsync() throws Exception {
    // Arrage.
    Generation generation = new Generation();
    generation.setText("This ice cream is sweet and delicious");
    CohereGenerateResponse generateResponse = new CohereGenerateResponse();
    generateResponse.setGenerations(ImmutableList.of(generation));
    when(CohereApiService.generateAsync(any()))
        .thenReturn(Futures.immediateFuture(generateResponse));

    // Act.
    ListenableFuture<SingleText> actualOutput =
        cohereGenerateProcessor.runAsync(SingleText.of("ice cream"));

    // Assert.
    assertEquals("This ice cream is sweet and delicious", actualOutput.get().getText());
  }
}
