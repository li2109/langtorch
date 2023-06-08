package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.hub.domain.TorchContextConfig;
import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.testclass.TorchHubClass;
import ai.knowly.langtorch.hub.testclass.TorchletClass;
import ai.knowly.langtorch.hub.testclass.TorchletClassPrototype;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TorchContextTest {
  private TorchContext torchContext;

  @BeforeEach
  void setup() {
    TorchContextConfig config = TorchContextConfig.builder().setVerbose(false).build();
    torchContext = new TorchContext(config);
  }

  @Test
  void init_withNonTorchHubClass_throwsRequiredAnnotationNotFoundException() {
    // Arrange.
    Class<?> nonTorchHubClass = String.class;

    // Act and Assert.
    try {
      torchContext.init(nonTorchHubClass);
      Assertions.fail("Expected RequiredAnnotationNotFoundException");
    } catch (AnnotationNotFoundException e) {
      assertThat(e.getMessage()).contains("is not annotated with @TorchHub");
    }
  }

  @Test
  void getTorchlet_withNonTorchletClass_throwsRuntimeException() {
    // Arrange.
    Class<?> nonTorchletClass = String.class;

    // Act and Assert.
    try {
      torchContext.getTorchlet(nonTorchletClass);
      Assertions.fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("is not annotated with @Torchlet");
    }
  }

  @Test
  void getTorchlet_withNotDefinedTorchlet_throwsRuntimeException() {
    // Arrange.
    String nonDefinedTorchlet = "NonDefinedTorchlet";

    // Act and Assert.
    try {
      torchContext.getTorchlet(nonDefinedTorchlet);
      Assertions.fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("is not found in the context");
    }
  }

  @Test
  void getTorchlet_returnsSingletonTorchlet_whenTorchletScopeIsSingleton() {
    // Assuming TorchletClass is a singleton Torchlet class

    // Arrange.
    torchContext.init(TorchHubClass.class); // Suppose TorchHubClass is your hub class

    // Act
    Object torchlet1 = torchContext.getTorchlet(TorchletClass.class);
    Object torchlet2 = torchContext.getTorchlet(TorchletClass.class);

    // Assert
    assertThat(torchlet1).isSameInstanceAs(torchlet2);
  }

  @Test
  void getTorchlet_returnsNewInstance_whenTorchletScopeIsPrototype() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    torchContext.init(TorchHubClass.class); // Suppose TorchHubClass is your hub class

    // Act
    Object torchlet1 = torchContext.getTorchlet(TorchletClassPrototype.class);
    Object torchlet2 = torchContext.getTorchlet(TorchletClassPrototype.class);

    // Assert
    assertThat(torchlet1).isNotSameInstanceAs(torchlet2);
  }
}
