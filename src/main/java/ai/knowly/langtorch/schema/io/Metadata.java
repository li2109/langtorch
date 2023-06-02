package ai.knowly.langtorch.schema.io;

import java.util.HashMap;
import java.util.Map;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
public class Metadata {
    private final Map<String, String> value;

    private Metadata(Map<String, String> values) {
        this.value = values;
    }

    public static Metadata create() {
        return new Metadata(new HashMap<>());
    }

    public static Metadata create(Map<String, String> values) {
        return new Metadata(new HashMap<>(values));
    }

    public Metadata set(String key, String value) {
        this.value.put(key, value);
        return this;
    }

    public static Metadata copyOf(Map<String, String> values) {
        return new Metadata(new HashMap<>(values));
    }
}
