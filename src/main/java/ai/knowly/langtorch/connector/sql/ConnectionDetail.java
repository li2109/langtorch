package ai.knowly.langtorch.connector.sql;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "set")
public class ConnectionDetail {
  private String url;
  private String user;
  private String password;

  public Optional<String> getUrl() {
    return Optional.ofNullable(url);
  }

  public Optional<String> getUser() {
    return Optional.ofNullable(user);
  }

  public Optional<String> getPassword() {
    return Optional.ofNullable(password);
  }
}
