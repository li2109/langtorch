package ai.knowly.llm.base.chatmodel;

import ai.knowly.llm.base.BaseModel;import ai.knowly.llm.message.BaseChatMessage;
import java.util.List;

/** A chat model is a model that takes in a list of messages and returns a message. */
public abstract class BaseChatModel extends BaseModel {
  // A chat model takes in a list of messages and returns a message.
  public abstract BaseChatMessage run(List<BaseChatMessage> messages);

  // A chat model takes in a user message and returns a message.
  public abstract String run(String message);
}
