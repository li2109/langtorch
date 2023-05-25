package ai.knowly.langtorch.schema.io;

import java.util.List;
import java.util.Map;

public class Metadatas {

    private final List<Map<String, String>> values;

    public Metadatas(List<Map<String, String>> values) {
        this.values = values;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }
}
