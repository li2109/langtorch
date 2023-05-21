package ai.knowly.langtorch.schema.image;

import ai.knowly.langtorch.schema.io.Output;
import java.util.List;

public class Images implements Output {
  Long created;
  List<Image> imageData;

  private Images(Long created, List<Image> imageData) {
    this.created = created;
    this.imageData = imageData;
  }

  public static Images of(Long created, List<Image> images) {
    return new Images(created, images);
  }

  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public List<Image> getImageData() {
    return imageData;
  }

  public void setImageData(List<Image> imageData) {
    this.imageData = imageData;
  }
}
