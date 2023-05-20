package ai.knowly.langtorch.capability.local.v1;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** A class for the completion config. */
@Data
public class Input {
  @SerializedName("parameters")
  private List<Parameter> parameters;
}
