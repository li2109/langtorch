package ai.knowly.langtorch.schema.io;

import java.util.Optional;

public class DomainDocument implements Input, Output {

  private final String pageContent;
  private final Optional<Metadata> metadata;
  private final Optional<String> id;

  private DomainDocument(Builder builder) {
    this.pageContent = builder.pageContent;
    this.metadata = builder.metadata;
    this.id = builder.id;
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

  public static class Builder {
    private String pageContent;
    private Optional<Metadata> metadata = Optional.empty();
    private Optional<String> id = Optional.empty();

    public Builder setPageContent(String pageContent) {
      this.pageContent = pageContent;
      return this;
    }

    public Builder setMetadata(Optional<Metadata> metadata) {
      this.metadata = metadata;
      return this;
    }

    public Builder setId(Optional<String> id) {
      this.id = id;
      return this;
    }

    public DomainDocument build() {
      return new DomainDocument(this);
    }
  }
}
