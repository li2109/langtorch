package ai.knowly.langtorch.connector.youtube;

import ai.knowly.langtorch.connector.ConnectorOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Youtube. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class YoutubeConnectorOption implements ConnectorOption {
  private String link;
}
