package ai.knowly.langtoch.capability.unit.wrapper;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.capability.unit.LLMCapabilityUnit;
import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.llm.message.AssistantMessage;
import ai.knowly.langtoch.llm.message.Role;
import ai.knowly.langtoch.llm.message.UserMessage;
import ai.knowly.langtoch.memory.conversation.ConversationMemory;

/**
 * A class wraps a LLM capability unit that processes a String and returns a String from the LLM
 * provider.
 */
public class ConversationalLLMWrapperUnit extends CapabilityUnit<UserMessage, AssistantMessage> {
  private final LLMCapabilityUnit<String, String> capabilityUnit;
  private ConversationMemory conversationMemory;

  public ConversationalLLMWrapperUnit(BaseModel baseModel) {
    capabilityUnit = LLMCapabilityUnit.<String, String>builder().setModel(baseModel).build();
  }

  public ConversationalLLMWrapperUnit(BaseModel baseModel, ConversationMemory conversationMemory) {
    capabilityUnit = LLMCapabilityUnit.<String, String>builder().setModel(baseModel).build();
    this.conversationMemory = conversationMemory;
  }

  @Override
  public AssistantMessage run(UserMessage input) {
    StringBuilder inputBuilder = new StringBuilder();
    inputBuilder.append(
        "The following is a friendly conversation between a human and an AI. The AI is talkative"
            + " and provides lots of specific details from its context. If the AI does not know the"
            + " answer to a question, it truthfully says it does not know.");
    if (conversationMemory != null) {
      inputBuilder.append(conversationMemory.getMemoryContext());
    }

    inputBuilder.append(input.toString());
    String response = capabilityUnit.run(inputBuilder.toString());
    AssistantMessage assistantMessage = AssistantMessage.builder().setMessage(response).build();
    conversationMemory.add(Role.USER, input);
    conversationMemory.add(Role.ASSISTANT, assistantMessage);
    return assistantMessage;
  }
}
