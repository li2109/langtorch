package ai.knowly.langtorch.schema.io;

import java.util.Optional;

public class DomainDocument implements Input, Output {

    private final String pageContent;

    private final Optional<Metadata> metadata;

    public DomainDocument(String pageContent, Optional<Metadata> metadata) {
        this.pageContent = pageContent;
        this.metadata = metadata;
    }

    public String getPageContent() {
        return pageContent;
    }

    public Optional<Metadata> getMetadata() {
        return metadata;
    }
}
