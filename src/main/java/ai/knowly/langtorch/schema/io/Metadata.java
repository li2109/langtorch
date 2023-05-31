package ai.knowly.langtorch.schema.io;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Metadata {
    private final Map<String, String> value;

    public Metadata(Map<String, String> values) {
        this.value = values;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public static Metadata create() {
        return new Metadata(new HashMap<>());
    }

    public static Metadata create(Map<String, String> values) {
        Map<String, String> map = new HashMap<>(values);
        return new Metadata(map);
    }

    public Metadata set(String key, String value) {
        this.value.put(key, value);
        return this;
    }

    public static Metadata copyOf(Map<String, String> values) {
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
