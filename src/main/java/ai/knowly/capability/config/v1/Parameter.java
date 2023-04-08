package ai.knowly.capability.config.v1;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Parameter {
  @SerializedName("name")
  private String name;

  @SerializedName("description")
  private String description;

  @SerializedName("defaultValue")
  private String defaultValue;
}
