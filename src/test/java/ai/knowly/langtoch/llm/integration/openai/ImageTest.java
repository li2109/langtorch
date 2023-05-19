package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageEditRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageVariationRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.Image;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ImageTest {

  static String filePath = "src/test/resources/penguin.png";
  static String fileWithAlphaPath = "src/test/resources/penguin_with_alpha.png";
  static String maskPath = "src/test/resources/mask.png";

  private String token;
  private OpenAIService service;

  @BeforeEach
  void setUp() {
    token = Utils.getOpenAIApiKeyFromEnv();
    service = new OpenAIService(token);
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createImageUrl() {
    CreateImageRequest createImageRequest =
        CreateImageRequest.builder().prompt("penguin").n(3).size("256x256").user("testing").build();

    List<Image> images = service.createImage(createImageRequest).getData();
    assertEquals(3, images.size());
    assertNotNull(images.get(0).getUrl());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createImageBase64() {
    CreateImageRequest createImageRequest =
        CreateImageRequest.builder()
            .prompt("penguin")
            .responseFormat("b64_json")
            .user("testing")
            .build();

    List<Image> images = service.createImage(createImageRequest).getData();
    assertEquals(1, images.size());
    assertNotNull(images.get(0).getB64Json());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createImageEdit() {
    CreateImageEditRequest createImageRequest =
        CreateImageEditRequest.builder()
            .prompt("a penguin with a red background")
            .responseFormat("url")
            .size("256x256")
            .user("testing")
            .n(2)
            .build();

    List<Image> images =
        service.createImageEdit(createImageRequest, fileWithAlphaPath, null).getData();
    assertEquals(2, images.size());
    assertNotNull(images.get(0).getUrl());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createImageEditWithMask() {
    CreateImageEditRequest createImageRequest =
        CreateImageEditRequest.builder()
            .prompt("a penguin with a red hat")
            .responseFormat("url")
            .size("256x256")
            .user("testing")
            .n(2)
            .build();

    List<Image> images = service.createImageEdit(createImageRequest, filePath, maskPath).getData();
    assertEquals(2, images.size());
    assertNotNull(images.get(0).getUrl());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createImageVariation() {
    CreateImageVariationRequest createImageVariationRequest =
        CreateImageVariationRequest.builder()
            .responseFormat("url")
            .size("256x256")
            .user("testing")
            .n(2)
            .build();

    List<Image> images =
        service.createImageVariation(createImageVariationRequest, filePath).getData();
    assertEquals(2, images.size());
    assertNotNull(images.get(0).getUrl());
  }
}
