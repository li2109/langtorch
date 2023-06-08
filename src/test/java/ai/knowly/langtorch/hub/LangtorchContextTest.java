package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.testclass.OrderService;
import ai.knowly.langtorch.hub.testclass.TakeoutService;
import ai.knowly.langtorch.hub.testclass.TorchHubClass;
import ai.knowly.langtorch.hub.testclass.TorchletClass;
import ai.knowly.langtorch.hub.testclass.TorchletClassPrototype;
import ai.knowly.langtorch.hub.testclass.TorchletClassWithUserSpecifiedName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LangtorchContextTest {
  private LangtorchContext langtorchContext;

  @BeforeEach
  void setup() {
    LangtorchHubConfig config = LangtorchHubConfig.builder().setVerbose(false).build();
    langtorchContext = new LangtorchContext(config);
  }

  @Test
  void init_withNonTorchHubClass_throwsRequiredAnnotationNotFoundException() {
    // Arrange.
    Class<?> nonTorchHubClass = String.class;

    // Act and Assert.
    try {
      langtorchContext.init(nonTorchHubClass);
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
      langtorchContext.getTorchlet(nonTorchletClass);
      Assertions.fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("is not found in the context");
    }
  }

  @Test
  void getTorchlet_withNotDefinedTorchlet_throwsRuntimeException() {
    // Arrange.
    String nonDefinedTorchlet = "NonDefinedTorchlet";

    // Act and Assert.
    try {
      langtorchContext.getTorchlet(nonDefinedTorchlet);
      Assertions.fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("is not found in the context");
    }
  }

  @Test
  void getTorchlet_returnsSingletonTorchlet_whenTorchletScopeIsSingleton() {
    // Assuming TorchletClass is a singleton Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet(TorchletClass.class);
    Object torchlet2 = langtorchContext.getTorchlet(TorchletClass.class);
    Object torchlet3 = langtorchContext.getTorchlet(OrderService.class);
    Object torchlet4 = langtorchContext.getTorchlet(OrderService.class);

    // Assert.
    assertThat(torchlet1).isSameInstanceAs(torchlet2);
    assertThat(torchlet3).isSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_returnsNewInstance_whenTorchletScopeIsPrototype() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet(TorchletClassPrototype.class);
    Object torchlet2 = langtorchContext.getTorchlet(TorchletClassPrototype.class);
    Object torchlet3 = langtorchContext.getTorchlet(TakeoutService.class);
    Object torchlet4 = langtorchContext.getTorchlet(TakeoutService.class);

    // Assert.
    assertThat(torchlet1).isNotSameInstanceAs(torchlet2);
    assertThat(torchlet3).isNotSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_byUserSpecifiedName() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet("torchlet-class-with-user-specified-name");

    // Assert.
    assertThat(torchlet1).isInstanceOf(TorchletClassWithUserSpecifiedName.class);
  }
}
