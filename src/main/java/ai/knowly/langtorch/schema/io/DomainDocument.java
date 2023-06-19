package ai.knowly.langtorch.schema.io;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DomainDocument implements Input, Output {
  @NonNull private String pageContent;
  private Metadata metadata;
  private String id;
  private Optional<Double> similarityScore;

  public Optional<Metadata> getMetadata() {
    return Optional.ofNullable(metadata);
  }

  public Optional<String> getId() {
    return Optional.ofNullable(id);
  }

  public void setSimilarityScore(Optional<Double> similarityScore) {
    this.similarityScore = similarityScore;
  }

  public Optional<Double> getSimilarityScore() {
    return similarityScore;
  }
}
