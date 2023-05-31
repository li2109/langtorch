package ai.knowly.langtorch.schema.io;

import java.util.Optional;

public class DomainDocument implements Input, Output {

  private final String pageContent;

  private final Optional<Metadata> metadata;

  private final Optional<String> id;

  public DomainDocument(String pageContent, Optional<Metadata> metadata, Optional<String> id) {
    this.pageContent = pageContent;
    this.metadata = metadata;
    this.id = id;
  }

  public Optional<String> getId() {
    return id;
  }

  public String getPageContent() {
    return pageContent;
  }

  public Optional<Metadata> getMetadata() {
    return metadata;
  }
}
