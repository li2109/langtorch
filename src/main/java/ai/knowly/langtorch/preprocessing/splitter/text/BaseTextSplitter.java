package ai.knowly.langtorch.preprocessing.splitter.text;

import java.util.List;

public interface BaseTextSplitter<S extends SplitterOption> {
  List<String> splitText(S option);
}
