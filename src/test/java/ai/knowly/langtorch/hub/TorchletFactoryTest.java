package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TorchletFactoryTest {
  TorchletFactory torchletFactory;

  @BeforeEach
  void setUp() {
    torchletFactory = TorchletFactory.create();
  }

  @Test
  void addTorchletDefinition_AddsToMap() {
    // Arrange.
    Class<?> clazz = String.class;
    Object torchletDefinition = new Object();

    // Act.
    torchletFactory.addTorchletDefinition(clazz, torchletDefinition);
    Set<Object> definitions = torchletFactory.getTorchletDefinitions(clazz);

    // Assert.
    assertThat(definitions).contains(torchletDefinition);
  }

  @Test
  void addSingletonTorchlet_AddsToMap() {
    // Arrange.
    Class<?> clazz = Integer.class;
    Object torchlet = new Object();

    // Act.
    torchletFactory.addSingletonTorchlet(clazz, torchlet);
    Set<Object> torchlets = torchletFactory.getSingletonTorchlets(clazz);

    // Assert.
    assertThat(torchlets).contains(torchlet);
  }

  @Test
  void addTorchletDefinition_WithNullClass_DoesNotAdd() {
    // Arrange.
    Class<?> clazz = null;
    Object torchletDefinition = new Object();

    // Act.
    torchletFactory.addTorchletDefinition(clazz, torchletDefinition);
    Set<Object> definitions = torchletFactory.getTorchletDefinitions(String.class);

    // Assert.
    assertThat(definitions).doesNotContain(torchletDefinition);
  }

  @Test
  void addSingletonTorchlet_WithNullClass_DoesNotAdd() {
    // Arrange.
    Class<?> clazz = null;
    Object torchlet = new Object();

    // Act.
    torchletFactory.addSingletonTorchlet(clazz, torchlet);
    Set<Object> torchlets = torchletFactory.getSingletonTorchlets(Integer.class);

    // Assert.
    assertThat(torchlets).doesNotContain(torchlet);
  }

  @Test
  void getTorchletDefinitions_WithEmptyClass_ReturnsEmptySet() {
    // Arrange.
    Class<?> clazz = Double.class;

    // Act.
    Set<Object> definitions = torchletFactory.getTorchletDefinitions(clazz);

    // Assert.
    assertThat(definitions).isEmpty();
  }

  @Test
  void getSingletonTorchlets_WithEmptyClass_ReturnsEmptySet() {
    // Arrange.
    Class<?> clazz = Double.class;

    // Act.
    Set<Object> torchlets = torchletFactory.getSingletonTorchlets(clazz);

    // Assert.
    assertThat(torchlets).isEmpty();
  }

  @Test
  void getInstanceByInterface() {
    // Arrange.
    Class<?> clazz = InterfaceA.class;
    Object torchlet = new InterfaceAImpl();

    // Act.
    torchletFactory.addSingletonTorchlet(clazz, torchlet);
    Set<Object> torchlets = torchletFactory.getSingletonTorchlets(clazz);

    // Assert.
    assertThat(torchlets).contains(torchlet);
  }

  @Test
  void getInstanceBySuperClass() {
    // Arrange.
    Class<?> clazz = SuperClass.class;
    Object torchlet = new SubClass();

    // Act.
    torchletFactory.addSingletonTorchlet(clazz, torchlet);
    Set<Object> torchlets = torchletFactory.getSingletonTorchlets(clazz);

    // Assert.
    assertThat(torchlets).contains(torchlet);
  }

  private interface InterfaceA {
    int methodA();
  }

  private static class SuperClass {
    int superClassMethod() {
      return 0;
    }
  }

  private static class SubClass extends SuperClass {
    int subClassMethod() {
      return 0;
    }
  }

  private class InterfaceAImpl implements InterfaceA {
    @Override
    public int methodA() {
      return 0;
    }
  }
}
