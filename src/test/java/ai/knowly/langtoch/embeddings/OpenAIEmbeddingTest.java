package ai.knowly.langtoch.embeddings;

import ai.knowly.langtoch.llm.processor.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtoch.schema.embeddings.Embeddings;
import ai.knowly.langtoch.schema.io.EmbeddingInput;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.OpenAiApi;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenAIEmbeddingTest {

    @Mock
    private OpenAiApi OpenAiApi;
    private OpenAIEmbeddingsProcessor openAIEmbeddingsProcessor;

    @Before
    public void setUp() {
        openAIEmbeddingsProcessor = new OpenAIEmbeddingsProcessor(OpenAiApi);
    }

    @Test
    public void testOpenAiEmbeddings() {

        String model = "model";
        List<String> inputData = new ArrayList<>();
        String user = "user";

        EmbeddingInput input = new EmbeddingInput(model, inputData, user);

        when(OpenAiApi.createEmbeddings(any()))
                .thenReturn(
                        Single.just(
                                OpenAIServiceTestingUtils.Embeddings.createEmbeddingResult()
                        )
                );

        Embeddings result = openAIEmbeddingsProcessor.run(input);

        assertEquals("OPEN_AI", result.getType().name());
        assertNotNull(result);
    }



}
