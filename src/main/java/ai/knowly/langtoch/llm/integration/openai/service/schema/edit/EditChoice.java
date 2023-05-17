package ai.knowly.langtoch.llm.integration.openai.service.schema.edit;

import lombok.Data;

/**
 * An edit generated by OpenAi
 *
 * https://beta.openai.com/docs/api-reference/edits/create
 */
@Data
public class EditChoice {

    /**
     * The edited text.
     */
    String text;

    /**
     * This index of this completion in the returned list.
     */
    Integer index;
}
