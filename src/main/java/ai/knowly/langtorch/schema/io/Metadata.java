package ai.knowly.langtorch.schema.io;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.Map;
import java.util.Objects;

public class Metadata {
  private final MultiKeyMap<String, String> value;

  public Metadata(MultiKeyMap<String, String> values) {
    this.value = values;
  }

  public MultiKeyMap<String, String> getValue() {
    return value;
  }

  public static Metadata create() {
    return new Metadata(new MultiKeyMap<>());
  }

    public static Metadata create(Map<String, String> values) {
        MultiKeyMap<String, String> multiKeyMap = new MultiKeyMap<>();
        for (Map.Entry<String, String> entry: values.entrySet()) {
            multiKeyMap.put(entry.getKey(), "", entry.getValue());
        }
        return new Metadata(multiKeyMap);
    }

    public Metadata set(MultiKey<String> key, String value) {
        this.value.put(key, value);
        return this;
    }

  public static Metadata copyOf(MultiKeyMap<String, String> values) {
    return new Metadata(values);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Metadata other = (Metadata) obj;
    return Objects.equals(value, other.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
