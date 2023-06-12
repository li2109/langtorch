package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.exception.TorchletAlreadyExistsException;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.testclass.package1.OrderService1;
import ai.knowly.langtorch.hub.testclass.package1.TorchHubClass1;
import ai.knowly.langtorch.hub.testclass.package1.TorchletClass1;
import ai.knowly.langtorch.hub.testclass.package10.TakeoutService10;
import ai.knowly.langtorch.hub.testclass.package10.TorchHubClass10;
import ai.knowly.langtorch.hub.testclass.package11.TakeoutService11;
import ai.knowly.langtorch.hub.testclass.package11.TakeoutServiceAbstractClass;
import ai.knowly.langtorch.hub.testclass.package11.TorchHubClass11;
import ai.knowly.langtorch.hub.testclass.package2.TakeoutService2;
import ai.knowly.langtorch.hub.testclass.package2.TorchHubClass2;
import ai.knowly.langtorch.hub.testclass.package2.TorchletClassPrototype2;
import ai.knowly.langtorch.hub.testclass.package3.TorchHubClass3;
import ai.knowly.langtorch.hub.testclass.package3.TorchletClassWithUserSpecifiedName;
import ai.knowly.langtorch.hub.testclass.package4.CombinedService4;
import ai.knowly.langtorch.hub.testclass.package4.TorchHubClass4;
import ai.knowly.langtorch.hub.testclass.package5.CombinedService5;
import ai.knowly.langtorch.hub.testclass.package5.TorchHubClass5;
import ai.knowly.langtorch.hub.testclass.package7.TorchHubClass7;
import ai.knowly.langtorch.hub.testclass.package8.TorchHubClass8;
import ai.knowly.langtorch.hub.testclass.package9.TakeoutService;
import ai.knowly.langtorch.hub.testclass.package9.TakeoutService9;
import ai.knowly.langtorch.hub.testclass.package9.TorchHubClass9;
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
      langtorchContext.getTorchletByType(nonTorchletClass);
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
      langtorchContext.getTorchletByName(nonDefinedTorchlet);
      Assertions.fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("is not found in the context");
    }
  }

  @Test
  void getTorchlet_returnsSingletonTorchlet_whenTorchletScopeIsSingleton() {
    // Arrange.
    langtorchContext.init(TorchHubClass1.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchletByType(TorchletClass1.class);
    Object torchlet2 = langtorchContext.getTorchletByType(TorchletClass1.class);
    Object torchlet3 = langtorchContext.getTorchletByType(OrderService1.class);
    Object torchlet4 = langtorchContext.getTorchletByType(OrderService1.class);

    // Assert.
    assertThat(torchlet1).isSameInstanceAs(torchlet2);
    assertThat(torchlet3).isSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_returnsNewInstance_whenTorchletScopeIsPrototype() {

    // Arrange.
    langtorchContext.init(TorchHubClass2.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 = langtorchContext.getTorchletByType(TorchletClassPrototype2.class);
    Object torchlet2 = langtorchContext.getTorchletByType(TorchletClassPrototype2.class);
    Object torchlet3 = langtorchContext.getTorchletByType(TakeoutService2.class);
    Object torchlet4 = langtorchContext.getTorchletByType(TakeoutService2.class);

    // Assert.
    assertThat(torchlet1).isNotSameInstanceAs(torchlet2);
    assertThat(torchlet3).isNotSameInstanceAs(torchlet4);
  }

  @Test
  void getTorchlet_byUserSpecifiedName() {

    // Arrange.
    langtorchContext.init(TorchHubClass3.class); // Suppose TorchHubClass is your hub class

    // Act.
    Object torchlet1 =
        langtorchContext.getTorchletByName("torchlet-class-with-user-specified-name");

    // Assert.
    assertThat(torchlet1).isInstanceOf(TorchletClassWithUserSpecifiedName.class);
  }

  @Test
  void getSingletonFieldsFromFieldInjectionAndConstructorInjection() {
    // Arrange.
    langtorchContext.init(TorchHubClass4.class); // Suppose TorchHubClass is your hub class

    // Act.
    CombinedService4 combinedService =
        (CombinedService4) langtorchContext.getTorchletByType(CombinedService4.class);

    // Assert.
    assertThat(combinedService.getTakeoutService4FromField())
        .isSameInstanceAs(combinedService.getTakeoutService4FromConstructor());
  }

  @Test
  void getPrototypeFieldsFromFieldInjectionAndConstructorInjection() {
    // Arrange.
    langtorchContext.init(TorchHubClass5.class); // Suppose TorchHubClass is your hub class

    // Act.
    CombinedService5 combinedService =
        (CombinedService5) langtorchContext.getTorchletByType(CombinedService5.class);

    // Assert.
    assertThat(combinedService.getTakeoutService5FromConstructor())
        .isNotSameInstanceAs(combinedService.getTakeoutService5FromField());
  }

  @Test
  void getByInterface() {
    // Arrange.
    langtorchContext.init(TorchHubClass9.class);

    // Act.
    Object takeoutService9 = langtorchContext.getTorchletByType(TakeoutService9.class);
    Object takeoutService9a = langtorchContext.getTorchletByType(TakeoutService9.class);
    Object takeoutService9FromInterface = langtorchContext.getTorchletByType(TakeoutService.class);

    // Assert.
    assertThat(takeoutService9).isInstanceOf(TakeoutService9.class);
    assertThat(takeoutService9a).isSameInstanceAs(takeoutService9);
    assertThat(takeoutService9FromInterface).isSameInstanceAs(takeoutService9);
  }

  @Test
  void getBySuperClass() {
    // Arrange.
    langtorchContext.init(TorchHubClass11.class);

    // Act.
    Object takeoutService11 = langtorchContext.getTorchletByType(TakeoutService11.class);
    Object takeoutService11a = langtorchContext.getTorchletByType(TakeoutService11.class);
    Object takeoutServiceFromSuperClass =
        langtorchContext.getTorchletByType(TakeoutServiceAbstractClass.class);

    // Assert.
    assertThat(takeoutService11).isInstanceOf(TakeoutService11.class);
    assertThat(takeoutService11a).isSameInstanceAs(takeoutService11);
    assertThat(takeoutServiceFromSuperClass).isSameInstanceAs(takeoutService11);
  }

  @Test
  void singleton_sameInstance() {
    // Arrange.
    langtorchContext.init(TorchHubClass10.class);

    // Act.
    Object takeoutService10 = langtorchContext.getTorchletByType(TakeoutService10.class);
    Object takeoutService10a = langtorchContext.getTorchletByType(TakeoutService10.class);

    // Assert.
    assertThat(takeoutService10).isSameInstanceAs(takeoutService10a);
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
