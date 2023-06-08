package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.TorchInject;
import ai.knowly.langtorch.hub.annotation.Torchlet;

@Torchlet
public class D {
  private final E e;
  private final C c;

  @TorchInject
  public D(E e, C c) {
    this.e = e;
    this.c = c;
  }

  public C getC() {
    return c;
  }
}
