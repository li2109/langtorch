package ai.knowly.langtoch.embeddings;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.processor.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtoch.schema.embeddings.Embeddings;
import ai.knowly.langtoch.schema.io.EmbeddingInput;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
final class OpenAIEmbeddingTest {

    @Mock
    private OpenAIService OpenAiApi;
    private OpenAIEmbeddingsProcessor openAIEmbeddingsProcessor;

    @Before
    void setUp() {
        openAIEmbeddingsProcessor = new OpenAIEmbeddingsProcessor(OpenAiApi);
    }

    @Test
    void testOpenAiEmbeddings() {

        String model = "model";
        List<String> inputData = new ArrayList<>();
        String user = "user";

        EmbeddingInput input = new EmbeddingInput(model, inputData, user);

        when(OpenAiApi.createEmbeddings(any()))
                .thenReturn(
                        OpenAIServiceTestingUtils.Embeddings.createEmbeddingResult()
                );

        Embeddings result = openAIEmbeddingsProcessor.run(input);

        assertEquals("OPEN_AI", result.getType().name());
        assertNotNull(result);
    }



}
