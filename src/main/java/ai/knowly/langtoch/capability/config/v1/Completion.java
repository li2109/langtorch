package ai.knowly.langtoch.capability.config.v1;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** A class for the embedded completion config. */
@Data
public class Completion {
  @SerializedName("max_tokens")
  private int maxTokens;

  @SerializedName("temperature")
  private double temperature;

  @SerializedName("top_p")
  private double topP;

  @SerializedName("presence_penalty")
  private double presencePenalty;

  @SerializedName("frequency_penalty")
  private double frequencyPenalty;

  @SerializedName("stop_sequences")
  private List<String> stopSequences;
}
