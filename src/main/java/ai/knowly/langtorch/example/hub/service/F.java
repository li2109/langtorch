package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.TorchInject;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.domain.TorchScope;
import ai.knowly.langtorch.hub.domain.TorchScopeValue;

@Torchlet
@TorchScope(value = TorchScopeValue.PROTOTYPE)
public class F {
  private final C c;

  @TorchInject
  public F(C c) {
    this.c = c;
  }

  public C getC() {
    return c;
  }
}
