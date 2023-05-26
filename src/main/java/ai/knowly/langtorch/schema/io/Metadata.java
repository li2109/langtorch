package ai.knowly.langtorch.schema.io;

import org.apache.commons.collections4.map.MultiKeyMap;

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
}
