package ai.knowly.langtorch.connector.youtube;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Youtube. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class YoutubeReadOption implements ReadOption {
  private String link;
  private String apiKey;
}
