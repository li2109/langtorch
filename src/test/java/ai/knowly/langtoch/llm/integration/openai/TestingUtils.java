package ai.knowly.langtoch.llm.integration.openai;

import com.google.common.collect.ImmutableMap;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class TestingUtils {
  private static final String YAML_KEY_NAME_FOR_ENABLE_LLM_TEST_WITH_HTTP_REQUEST =
      "test.unit_test.enable_test_with_http_request";

  public static boolean testWithHttpRequestEnabled() {
    return (boolean) getTestingSetting().get(YAML_KEY_NAME_FOR_ENABLE_LLM_TEST_WITH_HTTP_REQUEST);
  }

  public static ImmutableMap<String, Object> getTestingSetting() {
    Yaml yaml = new Yaml();
    InputStream inputStream =
        TestingUtils.class.getClassLoader().getResourceAsStream("test-settings.yaml");
    Map<String, Object> obj = yaml.load(inputStream);
    return ImmutableMap.copyOf(obj);
  }
}
