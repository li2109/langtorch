package ai.knowly.langtorch.llm.minimax.schema.dto.completion;

import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import ai.knowly.langtorch.store.memory.MemoryValue;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class ChatCompletionRequest {

  /** The algorithm model being called can currently only take one value: abab5-chat */
  private String model;

  /**
   * Add emotional predictions to the response. Attention, when with_emotion=true and the request
   * context (input and output text) is long, the request will significantly slow down, reaching
   * several seconds
   */
  @Builder.Default private Boolean withEmotion = false;

  /**
   * Whether to return results through streaming in batches. If set to true, the results will be
   * returned in batches, with a separator for each character; If you want to use the standard SSE
   * response format, you can set use_standard_sse parameter is true
   */
  @Builder.Default private Boolean stream = false;

  /**
   * Whether to use the standard SSE format, when set to true, the results returned by streaming
   * will be separated by two alternate lines. This parameter only takes effect when stream is set
   * to true
   */
  @Builder.Default private Boolean useStandardSse = false;

  /**
   * How many results are generated; Not set to default to 1, with a maximum of 4. Due to beam_
   * Generating multiple results with width will consume more tokens
   */
  @Builder.Default private Integer beamWidth = 1;

  /**
   * The maximum length limit for dialogue background, characters, or functions is 4096 tokens, and
   * cannot be empty, Length affects interface performance
   */
  private String prompt;

  /** Dialogue Meta Information */
  private RoleMeta roleMeta;

  /** Dialogue content */
  private List<Message> messages;

  /**
   * If true, it indicates that the current request is set to continue mode, and the reply content
   * is the continuation of the last sentence of the incoming messages; At this point, the last
   * sentence from the sender is not limited to USER, but can also be BOT Assuming the last sentence
   * of the incoming messages is {"sender_type": "USER", "text": "The Gifted"}， The reply to the
   * completion may be 'must be useful'”
   */
  @Builder.Default private Boolean continueLastMessage = false;

  /**
   * The maximum number of generated tokens. It should be noted that this parameter does not affect
   * the generation effect of the model itself, but only achieves the function by truncating the
   * exceeded tokens. It is necessary to ensure that the number of tokens input in the previous text
   * and the sum of this value are less than 4096，Otherwise, the request will fail
   */
  @Builder.Default private Long tokensToGenerate = 128L;

  /**
   * Higher values will make the output more random, while lower values will make the output more
   * concentrated and deterministic. Suggest temperature and top_ p just only one of them at the
   * same time
   */
  @Builder.Default private Float temperature = 0.9f;

  /**
   * Sampling method, the smaller the numerical value, the stronger the certainty of the result; The
   * larger the value, the more random the result
   */
  @Builder.Default private Float topP = 0.95f;

  /**
   * Desensitize text information that is prone to privacy issues in the output, currently including
   * but not limited to email, domain name, link, ID number, home address, etc. The default is
   * false, which means that desensitization is enabled
   */
  @Builder.Default private Boolean skipInfoMask = false;

  @Data
  @Builder(toBuilder = true, setterPrefix = "set")
  public static class RoleMeta {

    /** User name */
    private String userName;

    /** AI synonym */
    private String botName;
  }

  @Data
  @Builder(toBuilder = true, setterPrefix = "set")
  public static class Message implements Input, Output, MemoryValue {

    /** Sender, currently only the following two legal values are allowed: USER、BOT */
    private String senderType;

    /** Message content length affects interface performance */
    private String text;
  }
}
