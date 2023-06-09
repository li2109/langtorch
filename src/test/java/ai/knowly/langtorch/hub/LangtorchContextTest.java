package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.exception.TorchletAlreadyExistsException;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.testclass.package1.OrderService1;
import ai.knowly.langtorch.hub.testclass.package1.TorchHubClass1;
import ai.knowly.langtorch.hub.testclass.package1.TorchletClass1;
import ai.knowly.langtorch.hub.testclass.package2.TakeoutService2;
import ai.knowly.langtorch.hub.testclass.package2.TorchHubClass2;
import ai.knowly.langtorch.hub.testclass.package2.TorchletClassPrototype2;
import ai.knowly.langtorch.hub.testclass.package3.TorchHubClass3;
import ai.knowly.langtorch.hub.testclass.package3.TorchletClassWithUserSpecifiedName;
import ai.knowly.langtorch.hub.testclass.package4.CombinedService4;
import ai.knowly.langtorch.hub.testclass.package4.TorchHubClass4;
import ai.knowly.langtorch.hub.testclass.package5.CombinedService5;
import ai.knowly.langtorch.hub.testclass.package5.TorchHubClass5;
import ai.knowly.langtorch.hub.testclass.package6.CombinedService6;
import ai.knowly.langtorch.hub.testclass.package6.TorchHubClass6;
import ai.knowly.langtorch.hub.testclass.package6.torchletProvider6.TakeoutService6;
import ai.knowly.langtorch.hub.testclass.package7.TorchHubClass7;
import ai.knowly.langtorch.hub.testclass.package8.TorchHubClass8;
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
    langtorchContext.init(TorchHubClass1.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet(TorchletClass1.class);
    Object torchlet2 = langtorchContext.getTorchlet(TorchletClass1.class);
    Object torchlet3 = langtorchContext.getTorchlet(OrderService1.class);
    Object torchlet4 = langtorchContext.getTorchlet(OrderService1.class);

    // Assert.
    assertThat(torchlet1).isSameInstanceAs(torchlet2);
    assertThat(torchlet3).isSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_returnsNewInstance_whenTorchletScopeIsPrototype() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass2.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet(TorchletClassPrototype2.class);
    Object torchlet2 = langtorchContext.getTorchlet(TorchletClassPrototype2.class);
    Object torchlet3 = langtorchContext.getTorchlet(TakeoutService2.class);
    Object torchlet4 = langtorchContext.getTorchlet(TakeoutService2.class);

    // Assert.
    assertThat(torchlet1).isNotSameInstanceAs(torchlet2);
    assertThat(torchlet3).isNotSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_byUserSpecifiedName() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass3.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchlet("torchlet-class-with-user-specified-name");

    // Assert.
    assertThat(torchlet1).isInstanceOf(TorchletClassWithUserSpecifiedName.class);
  }

  @Test
  void getSingletonFieldsFromFieldInjectionAndConstructorInjection() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass4.class); // Suppose TorchHubClass is your hub class

    // Act.
    CombinedService4 combinedService =
        (CombinedService4) langtorchContext.getTorchlet(CombinedService4.class);

    // Assert.
    assertThat(combinedService.getTakeoutService4FromField())
        .isSameInstanceAs(combinedService.getTakeoutService4FromConstructor());
  }

  @Test
  void getPrototypeFieldsFromFieldInjectionAndConstructorInjection() {
    // Assuming TorchletClassPrototype is a prototype Torchlet class

    // Arrange.
    langtorchContext.init(TorchHubClass5.class); // Suppose TorchHubClass is your hub class

    // Act.
    CombinedService5 combinedService =
        (CombinedService5) langtorchContext.getTorchlet(CombinedService5.class);

    // Assert.
    assertThat(combinedService.getTakeoutService5FromConstructor())
        .isNotSameInstanceAs(combinedService.getTakeoutService5FromField());
  }

  @Test
  void getFieldfromFieldInjectionAndConstructorInjectionWithNamed() {
    // Arrange.
    langtorchContext.init(TorchHubClass6.class);

    // Act.
    CombinedService6 combinedService =
        (CombinedService6) langtorchContext.getTorchlet(CombinedService6.class);
    TakeoutService6 takeoutService6a1 =
        (TakeoutService6) langtorchContext.getTorchlet("takeout-service-6-a");
    TakeoutService6 takeoutService6a2 =
        (TakeoutService6) langtorchContext.getTorchlet("takeout-service-6-a");

    TakeoutService6 takeoutService6b1 =
        (TakeoutService6) langtorchContext.getTorchlet("takeout-service-6-a");
    TakeoutService6 takeoutService6b2 =
        (TakeoutService6) langtorchContext.getTorchlet("takeout-service-6-a");

    // Assert.
    assertThat(combinedService.getTakeoutService6FromConstructor())
        .isNotSameInstanceAs(combinedService.getTakeoutService6FromField());
    assertThat(takeoutService6a1).isSameInstanceAs(takeoutService6a2);
    assertThat(takeoutService6b1).isSameInstanceAs(takeoutService6b2);
  }

  @Test
  void multipleTorchletWithSameName_fromTorchletProvider() {
    // Act and Assert.
    try {
      langtorchContext.init(TorchHubClass7.class);
    } catch (TorchletAlreadyExistsException e) {
      assertThat(e.getMessage()).contains("Torchlet takeout-service-7 already exists");
    }
  }

  @Test
  void multipleTorchletWithSameName_fromTorchletAnnotation() {
    // Act and Assert.
    try {
      langtorchContext.init(TorchHubClass8.class);
    } catch (TorchletAlreadyExistsException e) {
      assertThat(e.getMessage()).contains("Torchlet takeout-service-7 already exists");
    }
  }
}
