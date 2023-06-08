package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.TorchInject;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.domain.TorchScope;
import ai.knowly.langtorch.hub.domain.TorchScopeValue;

@Torchlet
@TorchScope(value = TorchScopeValue.PROTOTYPE)
public class C {
  private final A a;
  private final B b;

  @TorchInject
  public C(A a, B b) {
    this.a = a;
    this.b = b;
  }
}
