package ai.knowly.langtorch.preprocessing.splitter.text;

import java.util.List;

/**
 * The TextSplitter interface is an interface that represents a text splitter.
 */
public interface TextSplitter<S extends SplitterOption> {
  List<String> splitText(S option);
}
