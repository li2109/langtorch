package ai.knowly.langtorch.preprocessing.splitter.text;


/**
 * The SplitterOption class is an abstract class that represents a splitter option.
 */
public abstract class SplitterOption {
  String text;

  protected SplitterOption(String text) {
    this.text = text;
  }
}
