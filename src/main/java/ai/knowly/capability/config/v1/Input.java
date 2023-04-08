package ai.knowly.capability.config.v1;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class Input {
  @SerializedName("parameters")
  private List<Parameter> parameters;

  // Getters and setters for the fields
}
