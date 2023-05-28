package ai.knowly.langtorch.preprocessing.splitter.text;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;


public class WordSplitterTest {

    @Test
    public void testWordSplitter_realWorldText() {
        WordSplitter splitter = new WordSplitter(null, 1000, 100);

        List<String> result = splitter.splitText(sampleText());
        List<String> expectedResult = sampleTextExpectedSplit();

        for (int i = 0; i < result.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expectedResult.get(i));
        }
    }

    @Test
    public void testWordSplitter_splitByWordCount() {
        // Arrange.
        String text = "foo bar baz 123";
        WordSplitter splitter = new WordSplitter(" ", 7, 3);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo bar", "bar baz", "baz 123"));

        Truth.assertThat(result.size()).isEqualTo(expected.size());
        for (int i = 0; i < expected.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountWithNoEmptyDocuments() {
        // Arrange.
        String text = "foo bar";
        WordSplitter splitter = new WordSplitter(" ", 2, 0);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar"));

        for (int i = 0; i < expected.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountLongWords() {
        // Arrange.
        String text = "foo bar baz a a";
        WordSplitter splitter = new WordSplitter(" ", 3, 1);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar", "baz", "a a"));

        for (int i = 0; i < expected.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountShorterWordsFirst() {
        // Arrange.
        String text = "a a foo bar baz";
        WordSplitter splitter = new WordSplitter(" ", 3, 1);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("a a", "foo", "bar", "baz"));

        for (int i = 0; i < expected.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testCharacterTextSplitter_splitByCharactersSplitsNotFoundEasily() {
        // Arrange.
        String text = "foo bar baz 123";
        WordSplitter splitter = new WordSplitter(" ", 1, 0);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar", "baz", "123"));

        for (int i = 0; i < expected.size(); i++) {
            Truth.assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterTextSplitter_invalidArguments() {
        // Arrange.
        int chunkSize = 2;
        int chunkOverlap = 4;

        // Act.
        new WordSplitter(null, chunkSize, chunkOverlap);

        // Expect IllegalArgumentException to be thrown.
    }

    @Test
    public void testWordSplitter_createDocuments() {
        // Arrange.
        WordSplitter splitter = new WordSplitter(" ", 3, 0);
        List<DomainDocument> docsToSplit =
                Arrays.asList("foo bar", "baz").stream()
                        .map(text -> new DomainDocument(text, Optional.of(Metadata.create()))).collect(Collectors.toList());

        // Act.
        List<DomainDocument> docs = splitter.createDocumentsSplitFromList(docsToSplit);

        // Assert.
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", Optional.of(Metadata.create())),
                new DomainDocument("bar", Optional.of(Metadata.create())),
                new DomainDocument("baz", Optional.of(Metadata.create()))
        );

        Truth.assertThat(expectedDocs.size() == docs.size());
        for (int i = 0; i < docs.size(); i++) {
            Truth.assertThat(docs.get(i).getPageContent()).isEqualTo(expectedDocs.get(i).getPageContent());
        }
    }

    @Test
    public void testWordSplitter_createDocumentsWithMetadata() {
        // Arrange.
        WordSplitter splitter = new WordSplitter(" ", 3, 0);

        Metadata metadata = Metadata.create();

        metadata.getValue().put("source", "doc", "1");
        metadata.getValue().put("loc", "from", "1");
        metadata.getValue().put("loc", "to", "1");

        List<DomainDocument> docsToSplit =
                Arrays.asList("foo bar", "baz").stream()
                        .map(text -> new DomainDocument(text, Optional.of(metadata))).collect(Collectors.toList());

        // Act.
        List<DomainDocument> docs = splitter.createDocumentsSplitFromList(docsToSplit);

        // Assert.
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", Optional.of(metadata)),
                new DomainDocument("bar", Optional.of(metadata)),
                new DomainDocument("baz", Optional.of(metadata))
        );

        Truth.assertThat(docs.size()).isEqualTo(expectedDocs.size());
        for (int i = 0; i < docs.size(); i++) {
            Truth.assertThat(docs.get(i).getPageContent()).isEqualTo(expectedDocs.get(i).getPageContent());
            Truth.assertThat(docs.get(i).getMetadata()).isEqualTo(expectedDocs.get(i).getMetadata());
        }
    }


    private List<String> sampleTextExpectedSplit() {
        return Arrays.asList(
                "Langtorch one pager\n" +
                        "Langtorch is a Java framework that assists you in developing large language model applications. It is designed with reusability, composability and Fluent style in mind. It can aid you in developing workflows or pipelines that include large language models.\n" +
                        "\n" +
                        "Processor\n" +
                        "In Langtorch, we introduce the concept of a processor. A processor is a container for the smallest computational unit in Langtorch. The response produced by the processor can either originate from a large language model, such as OpenAI's GPT model(retrieved by rest api), or it could be a deterministic Java function.",
                "A processor is an interface that includes two functions: run() and runAsync(). Anything that implements these two functions can be considered a processor. For instance, a processor could be something that sends an HTTP request to OpenAI to invoke its GPT model and generate a response. It could also be a calculator function, where the input is 1+1, and the output is 2.\n" +
                        "Using this approach, we can conveniently add a processor, such as the soon-to-be-publicly-available Google PALM 2 API. At the same time, when we chain different processors together, we can leverage this to avoid some of the shortcomings of large language models (LLMs). For instance, when we want to implement a chatbot, if a user asks a mathematical question, we can parse this question using the LLM's capabilities into an input for our calculator to get an accurate answer, rather than letting the LLM come to a conclusion directly.",
                "Note: The processor is the smallest computational unit in Langtorch, so a processor is generally only allowed to process a single task. For example, it could have the ability to handle text completion, chat completion, or generate images based on a prompt. If the requirements are complex, such as first generating a company's slogan through text completion, and then generating an image based on the slogan, this should be accomplished by chaining different processors together, rather than completing everything within a single processor.\n" +
                        "\n" +
                        "Capability\n" +
                        "As previously mentioned, the processor is the smallest container of a computational unit, and often it is not sufficient to handle all situations. We need to enhance the processor!\n" +
                        "Here we introduce the concept of Capability. If the processor is likened to an internal combustion steam engine, then a Capability could be a steam train based on the steam engine, or a electricity generator based on the steam engine.",
                "Imagine that you are implementing a chatbot. If the processor is based on OpenAI's API, sending every user's input to the OpenAI GPT-4 model and returning its response, what would the user experience be like?\n" +
                        "\n" +
                        "The reason is that the chatbot does not incorporate chat history. Therefore, in capability, we can add memory (a simple implementation of memory is to put all conversation records into the request sent to OpenAI).\n" +
                        "\n" +
                        "Workflow(chaining Capabilities)\n" +
                        "To make the combination of capabilities easier, we introduce the concept of a Node Adapter, and we refer to capabilities nodes composition as a Capability graph.\n" +
                        "However, the capability graph can only be a Directed Acyclic Graph (DAG), i.e., there are no cycles allowed.\n" +
                        "\n" +
                        "The Node Adapter is primarily used for validation and optimization of the Capability graph. It wraps the capability and also includes some information about the Capability graph, such as the current node's globally unique ID, what the next nodes are, and so on."
        );

    }

    private String sampleText() {
        return "Langtorch one pager\n" +
                "Langtorch is a Java framework that assists you in developing large language model applications. It is designed with reusability, composability and Fluent style in mind. It can aid you in developing workflows or pipelines that include large language models.\n" +
                "\n" +
                "Processor\n" +
                "In Langtorch, we introduce the concept of a processor. A processor is a container for the smallest computational unit in Langtorch. The response produced by the processor can either originate from a large language model, such as OpenAI's GPT model(retrieved by rest api), or it could be a deterministic Java function.\n" +
                "\n" +
                "A processor is an interface that includes two functions: run() and runAsync(). Anything that implements these two functions can be considered a processor. For instance, a processor could be something that sends an HTTP request to OpenAI to invoke its GPT model and generate a response. It could also be a calculator function, where the input is 1+1, and the output is 2.\n" +
                "Using this approach, we can conveniently add a processor, such as the soon-to-be-publicly-available Google PALM 2 API. At the same time, when we chain different processors together, we can leverage this to avoid some of the shortcomings of large language models (LLMs). For instance, when we want to implement a chatbot, if a user asks a mathematical question, we can parse this question using the LLM's capabilities into an input for our calculator to get an accurate answer, rather than letting the LLM come to a conclusion directly.\n" +
                "\n" +
                "Note: The processor is the smallest computational unit in Langtorch, so a processor is generally only allowed to process a single task. For example, it could have the ability to handle text completion, chat completion, or generate images based on a prompt. If the requirements are complex, such as first generating a company's slogan through text completion, and then generating an image based on the slogan, this should be accomplished by chaining different processors together, rather than completing everything within a single processor.\n" +
                "\n" +
                "Capability\n" +
                "As previously mentioned, the processor is the smallest container of a computational unit, and often it is not sufficient to handle all situations. We need to enhance the processor!\n" +
                "Here we introduce the concept of Capability. If the processor is likened to an internal combustion steam engine, then a Capability could be a steam train based on the steam engine, or a electricity generator based on the steam engine.\n" +
                "\n" +
                "Imagine that you are implementing a chatbot. If the processor is based on OpenAI's API, sending every user's input to the OpenAI GPT-4 model and returning its response, what would the user experience be like?\n" +
                "\n" +
                "The reason is that the chatbot does not incorporate chat history. Therefore, in capability, we can add memory (a simple implementation of memory is to put all conversation records into the request sent to OpenAI).\n" +
                "\n" +
                "Workflow(chaining Capabilities)\n" +
                "To make the combination of capabilities easier, we introduce the concept of a Node Adapter, and we refer to capabilities nodes composition as a Capability graph.\n" +
                "However, the capability graph can only be a Directed Acyclic Graph (DAG), i.e., there are no cycles allowed.\n" +
                "\n" +
                "The Node Adapter is primarily used for validation and optimization of the Capability graph. It wraps the capability and also includes some information about the Capability graph, such as the current node's globally unique ID, what the next nodes are, and so on.";
    }


}
