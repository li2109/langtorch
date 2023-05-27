package ai.knowly.langtorch.schema.io;

import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.Objects;

public class Metadata {
    private final MultiKeyMap<String, String> value;

    public Metadata(MultiKeyMap<String, String> values) {
        this.value = values;
    }

    public MultiKeyMap<String, String> getValue() {
        return value;
    }

    public static Metadata createEmpty(){
        return new Metadata(new MultiKeyMap<>());
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
