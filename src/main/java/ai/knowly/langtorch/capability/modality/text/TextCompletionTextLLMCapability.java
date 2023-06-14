package ai.knowly.langtorch.capability.modality.text;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.processor.Processor;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

public class TextCompletionTextLLMCapability<I, O>
    implements TextLLMCapability<I, SingleText, SingleText, O> {

  private final Processor<SingleText, SingleText> processor;
  private final Parsers<I, SingleText, SingleText, O> parsers;

  @Inject
  public TextCompletionTextLLMCapability(
      Processor<SingleText, SingleText> processor, Parsers<I, SingleText, SingleText, O> parsers) {
    this.processor = processor;
    this.parsers = parsers;
  }

  @Override
  public SingleText preProcess(I inputData) {
    if (inputData instanceof SingleText) {
      return (SingleText) inputData;
    }

    return parsers
        .getInputParser()
        .map(parser -> parser.parse(inputData))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Input data is not a SingleText and no input parser is present."));
  }

  @Override
  public O postProcess(SingleText outputData) {
    return parsers
        .getOutputParser()
        .map(parser -> parser.parse(outputData))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Output data is not a SingleText and no output parser is present."));
  }

  @Override
  public O run(I inputData) {
    return postProcess(processor.run(preProcess(inputData)));
  }

  @Override
  public ListenableFuture<O> runAsync(I inputData) {
    return FluentFuture.from(immediateFuture(inputData)).transform(this::run, directExecutor());
  }
}
