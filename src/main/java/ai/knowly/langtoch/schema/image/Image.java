package ai.knowly.langtoch.schema.image;

import ai.knowly.langtoch.schema.io.Output;

public class Image implements Output {
  private final String url;

  private Image(String url) {
    this.url = url;
  }

  public static Image of(String url) {
    return new Image(url);
  }

  public String getUrl() {
    return url;
  }
}
