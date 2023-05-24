package ai.knowly.langtorch.schema.io;

import javax.annotation.Nullable;
import java.util.Map;

public class DomainDocument implements Input, Output {

    private final String pageContent;

    @Nullable
    private final Map<String, String> metadata;

    public DomainDocument(String pageContent, @Nullable  Map<String, String> metadata) {
        this.pageContent = pageContent;
        this.metadata = metadata;
    }

    public String getPageContent() {
        return pageContent;
    }

    public Map<String, String>  getMetadata() {
        return metadata;
    }
}
