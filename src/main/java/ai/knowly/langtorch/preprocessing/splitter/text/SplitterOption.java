package ai.knowly.langtorch.preprocessing.splitter.text;

public abstract class SplitterOption {
  String text;

  protected SplitterOption(String text) {
    this.text = text;
  }
}
