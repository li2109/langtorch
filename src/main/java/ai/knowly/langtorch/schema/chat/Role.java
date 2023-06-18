package ai.knowly.langtorch.schema.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/** A enum for the role of a message. */
public enum Role {
  /** openai role */
  SYSTEM("system"),
  USER("user"),
  ASSISTANT("assistant"),
  FUNCTION("function"),
  /** minimax role */
  MINIMAX_USER("USER"),
  MINIMAX_BOT("BOT");

  @JsonValue
  @JsonSerialize(using = ToStringSerializer.class)
  private String value;

  Role(String value) {
    this.value = value;
  }

  @JsonCreator
  public static Role fromString(String value) {
    for (Role role : Role.values()) {
      if (role.value.equals(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Invalid value: " + value);
  }

  @Override
  public String toString() {
    return value;
  }
}
