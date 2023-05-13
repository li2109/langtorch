package ai.knowly.langtoch.schema.image;

import ai.knowly.langtoch.schema.io.Output;
import java.util.List;

public class Images implements Output {
  Long created;
  List<Image> images;

  private Images(Long created, List<Image> images) {
    this.created = created;
    this.images = images;
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

  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }
}
