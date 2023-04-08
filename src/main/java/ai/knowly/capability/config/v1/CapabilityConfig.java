package ai.knowly.capability.config.v1;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A class for the capability config. */
@Data
public class CapabilityConfig {
  @SerializedName("schema")
  private int schema;

  @SerializedName("description")
  private String description;

  @SerializedName("type")
  private String type;

  @SerializedName("completion")
  private Completion completion;

  @SerializedName("input")
  private Input input;
}
