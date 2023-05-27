package ai.knowly.langtorch.preprocessing.splitter.text;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.util.*;


public class TextSplitterTest {

    @Test
    public void testCharacterTextSplitter_splitByCharacterCount() {
        // Arrange.
        String text = "foo bar baz 123";
        WordSplitter splitter = new WordSplitter(" ", 7, 3);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo bar", "bar baz", "baz 123"));

        Truth.assertThat(Objects.equals(expected, result));
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountWithNoEmptyDocuments() {
        // Arrange.
        String text = "i,love,langtorchisveryxxxxxxxxx";
        WordSplitter splitter = new WordSplitter(" ", 2, 0);

        // Act.
        List<String> result = splitter.splitText(text);

        // Assert.
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar"));

        Truth.assertThat(Objects.equals(expected, result));
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

        Truth.assertThat(Objects.equals(expected, result));
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

        Truth.assertThat(Objects.equals(expected, result));
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

        Truth.assertThat(Objects.equals(expected, result));
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
    public void testCharacterTextSplitter_createDocuments() {
        // Arrange.
        List<String> texts = Arrays.asList("foo bar", "baz");
        WordSplitter splitter = new WordSplitter(" ", 3, 0);
        Metadata metadata = Metadata.createEmpty();

        List<Metadata> metadatas = Arrays.asList(metadata, metadata);

        // Act.
        List<DomainDocument> docs = splitter.createDocuments(texts, Optional.of(metadatas));

        // Assert.
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", Optional.of(metadata)),
                new DomainDocument("bar", Optional.of(metadata)),
                new DomainDocument("baz", Optional.of(metadata))
        );

        Truth.assertThat(expectedDocs.size() == docs.size());
        for (int i = 0; i < docs.size(); i++) {
            Truth.assertThat(Objects.equals(docs.get(i).getPageContent(), expectedDocs.get(i).getPageContent()));
        }
    }

    @Test
    public void testCharacterTextSplitter_createDocumentsWithMetadata() {
        // Arrange.
        List<String> texts = Arrays.asList("foo bar", "baz");
        WordSplitter splitter = new WordSplitter(" ", 3, 0);


        Metadata metadata = Metadata.createEmpty();

        metadata.getValue().put("source", "doc", "1");
        metadata.getValue().put("loc", "from", "1");
        metadata.getValue().put("loc", "to", "1");

        List<Metadata> metadataList = Arrays.asList(metadata, metadata);

        Optional<List<Metadata>> metadatas = Optional.of(metadataList);

        // Act.
        List<DomainDocument> docs = splitter.createDocuments(texts, metadatas);

        // Assert.
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", Optional.of(metadata)),
                new DomainDocument("bar", Optional.of(metadata)),
                new DomainDocument("baz", Optional.of(metadata))
        );

        Truth.assertThat(expectedDocs.size() == docs.size());
        for (int i = 0; i < docs.size(); i++) {
            Truth.assertThat(Objects.equals(docs.get(i).getPageContent(), expectedDocs.get(i).getPageContent()));
        }
    }


    @Test
    public void testRecursiveCharacterTextSplitter_iterativeTextSplitter() {
        // Arrange.
        String text = "Hi.\n\nI'm Harrison.\n\nHow? Are? You?\nOkay then f f f f.\nThis is a weird text to write, but gotta test the splittingggg some how.\n\nBye!\n\n-H.";
        RecursiveWordTextSplitter splitter = new RecursiveWordTextSplitter(null, 10, 1);

        // Act.
        List<String> output = splitter.splitText(text);

        // Assert.
        List<String> expectedOutput = Arrays.asList(
                "Hi.",
                "I'm",
                "Harrison.",
                "How? Are?",
                "You?",
                "Okay then f",
                "f f f f.",
                "This is a",
                "a weird",
                "text to",
                "write, but",
                "gotta test",
                "the",
                "splittingg",
                "ggg",
                "some how.",
                "Bye!\n\n-H."
        );

        Truth.assertThat(Objects.equals(expectedOutput, output));
    }

    @Test
    public void testTextSplitter_iterativeTextSplitter_linesLoc() {
        // Arrange.
        String text = "Hi.\nI'm Harrison.\n\nHow?\na\nb";
        RecursiveWordTextSplitter splitter = new RecursiveWordTextSplitter(null, 20, 1);

        Optional<List<Metadata>> metadatas = Optional.ofNullable(null);
        // Act.
        List<DomainDocument> docs = splitter.createDocuments(Collections.singletonList(text), metadatas);

        // Assert.
        DomainDocument doc1 = new DomainDocument("Hi.\nI'm Harrison.", null);
        DomainDocument doc2 = new DomainDocument("How?\na\nb", null);
        List<DomainDocument> expectedDocs = Arrays.asList(doc1, doc2);

        Truth.assertThat(expectedDocs.size() == docs.size());
        Truth.assertThat(Objects.equals(expectedDocs.get(0).getPageContent(), docs.get(0).getPageContent()));
        Truth.assertThat(Objects.equals(expectedDocs.get(1).getPageContent(), docs.get(1).getPageContent()));
    }


}
