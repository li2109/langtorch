package ai.knowly.langtorch.preprocessing.splitter.text;

import java.util.List;

public interface TextSplitter<S extends SplitterOption> {
  List<String> splitText(S option);
}
