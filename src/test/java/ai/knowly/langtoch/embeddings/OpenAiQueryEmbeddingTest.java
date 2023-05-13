package ai.knowly.langtoch.embeddings;

import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.embedding.EmbeddingResult;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Objects;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenAiQueryEmbeddingTest {

    @Mock
    private OpenAiApi OpenAiApi;
    private OpenAiQueryEmbedding openAiQueryEmbedding;

    @Before
    public void setUp() {
        openAiQueryEmbedding = new OpenAiQueryEmbedding(OpenAiApi);
    }


    @Test
    public void testOpenAiEmbedQuery() {

        String inputData = "Hello";

        when(OpenAiApi.createEmbeddings(any()))
                .thenReturn(
                        Single.just(
                                OpenAIServiceTestingUtils.Embeddings.createQueryEmbeddingResult(inputData)
                        )
                );

        EmbeddingResult result = openAiQueryEmbedding.run(inputData);

        assertEquals("list", result.getObject());
    }



}
