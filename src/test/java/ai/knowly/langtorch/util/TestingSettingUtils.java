package ai.knowly.langtorch.util;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/** A utility class for reading testing settings. */
public class TestingSettingUtils {
  private static final String PINECONE_VECTOR_STORE_LIVE_TRAFFIC_TEST =
      "test.unit-test.live-traffic.vector-store-service.pinecone";

  private static final String OPENAI_LLM_SERVICE_LIVE_TRAFFIC_TEST =
      "test.unit-test.live-traffic.llm-service.openai";

  private static final String MINIMAX_LLM_SERVICE_LIVE_TRAFFIC_TEST =
      "test.unit-test.live-traffic.llm-service.minimax";

  private static final String COHERE_LLM_SERVICE_LIVE_TRAFFIC_TEST =
      "test.unit-test.live-traffic.llm-service.cohere";

  public static boolean enablePineconeVectorStoreLiveTrafficTest() {
    return readBooleanSetting(PINECONE_VECTOR_STORE_LIVE_TRAFFIC_TEST);
  }

  public static boolean enableOpenAILLMServiceLiveTrafficTest() {
    return readBooleanSetting(OPENAI_LLM_SERVICE_LIVE_TRAFFIC_TEST);
  }

  public static boolean enableMiniMaxLLMServiceLiveTrafficTest() {
    return readBooleanSetting(MINIMAX_LLM_SERVICE_LIVE_TRAFFIC_TEST);
  }

  public static boolean enableCohereLLMServiceLiveTrafficTest() {
    return readBooleanSetting(COHERE_LLM_SERVICE_LIVE_TRAFFIC_TEST);
  }

  public static ImmutableMap<String, Object> getTestingSetting() {
    Yaml yaml = new Yaml();
    InputStream inputStream =
        TestingSettingUtils.class.getClassLoader().getResourceAsStream("test_settings.yaml");
    Map<String, Object> obj = yaml.load(inputStream);
    return ImmutableMap.copyOf(obj);
  }

  private static Object getNestedValue(ImmutableMap<String, Object> map, Iterable<String> keys) {
    Map<String, Object> currentMap = map;
    Object value = null;
    for (String key : keys) {
      value = currentMap.get(key);
      if (value instanceof Map) {
        currentMap = (Map<String, Object>) value;
      }
    }
    return value;
  }

  private static boolean readBooleanSetting(String settingName) {
    ImmutableMap<String, Object> nestedValueMap = getTestingSetting();
    Object value = getNestedValue(nestedValueMap, Splitter.on(".").split(settingName));
    if (value == null) {
      return false;
    }
    return (boolean) value;
  }
}
