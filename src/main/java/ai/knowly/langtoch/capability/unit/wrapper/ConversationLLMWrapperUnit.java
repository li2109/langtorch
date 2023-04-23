//package ai.knowly.langtoch.capability.unit.wrapper;
//
//import ai.knowly.langtoch.capability.unit.CapabilityUnit;
//import ai.knowly.langtoch.capability.unit.LLMCapabilityUnit;
//import ai.knowly.langtoch.llm.processor.Processor;
//import ai.knowly.langtoch.llm.message.AssistantMessage;
//import ai.knowly.langtoch.llm.message.Role;
//import ai.knowly.langtoch.llm.message.UserMessage;
//import ai.knowly.langtoch.memory.conversation.ConversationMemory;
//import com.google.common.flogger.FluentLogger;
//
///**
// * A class wraps a LLM capability unit that processes a String and returns a String from the LLM
// * provider.
// */
//public class ConversationLLMWrapperUnit extends CapabilityUnit<UserMessage, AssistantMessage> {
//  private static final String DEFAULT_CONVERSATION_PROMPT =
//      "The following is a friendly conversation between a human and an AI. The AI is talkative"
//          + " and provides lots of specific details from its context. If the AI does not know the"
//          + " answer to a question, it truthfully says it does not know.\n\n";
//
//  private final LLMCapabilityUnit<String, String> capabilityUnit;
//
//  private final FluentLogger logger = FluentLogger.forEnclosingClass();
//  private ConversationMemory conversationMemory;
//
//  public ConversationLLMWrapperUnit(Processor processor) {
//    capabilityUnit = LLMCapabilityUnit.<String, String>builder().setModel(processor).build();
//  }
//
//  public ConversationLLMWrapperUnit(Processor processor, ConversationMemory conversationMemory) {
//    capabilityUnit = LLMCapabilityUnit.<String, String>builder().setModel(processor).build();
//    this.conversationMemory = conversationMemory;
//  }
//
//  @Override
//  public AssistantMessage run(UserMessage userMessage) {
//    StringBuilder inputBuilder = new StringBuilder();
//    inputBuilder.append(DEFAULT_CONVERSATION_PROMPT);
//
//    if (conversationMemory != null) {
//      inputBuilder.append(conversationMemory.getMemoryContext());
//    }
//
//    inputBuilder.append(userMessage.getMessage());
//    logger.atInfo().log(inputBuilder.toString());
//
//    String response = capabilityUnit.run(inputBuilder.toString());
//    AssistantMessage assistantMessage = AssistantMessage.builder().setMessage(response).build();
//    conversationMemory.add(Role.USER, userMessage);
//    conversationMemory.add(Role.ASSISTANT, assistantMessage);
//    return assistantMessage;
//  }
//}
