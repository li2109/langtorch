package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.domain.TorchScope;
import ai.knowly.langtorch.hub.domain.TorchScopeValue;

@Torchlet
@TorchScope(value = TorchScopeValue.PROTOTYPE)
public class A {}
