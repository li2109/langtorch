package ai.knowly.langtorch.loader.youtube;

import ai.knowly.langtorch.loader.LoadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Youtube. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class YoutubeLoadOption implements LoadOption {
  private String link;
}
