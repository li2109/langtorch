package ai.knowly.langtorch.parser;

import ai.knowly.langtorch.parser.textsplitter.CharacterTextSplitter;
import ai.knowly.langtorch.parser.textsplitter.RecursiveCharacterTextSplitter;
import ai.knowly.langtorch.parser.textsplitter.TextSplitter;
import ai.knowly.langtorch.schema.io.DomainDocument;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextSplitterTest {

    @Test
    public void testCharacterTextSplitter_splitByCharacterCount(){
        // Arrange
        String text = "foo bar baz 123";
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 7, 3);

        // Act
        List<String> result = splitter.splitText(text);

        // Assert
        List<String> expected = new  ArrayList<>(Arrays.asList("foo bar", "bar baz", "baz 123"));

        assertEquals(expected, result);
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountWithNoEmptyDocuments() {
        // Arrange
        String text = "foo bar";
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 2, 0);

        // Act
        List<String> result = splitter.splitText(text);

        // Assert
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar"));

        assertEquals(expected, result);
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountLongWords() {
        // Arrange
        String text = "foo bar baz a a";
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 3, 1);

        // Act
        List<String> result = splitter.splitText(text);

        // Assert
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar", "baz", "a a"));

        assertEquals(expected, result);
    }

    @Test
    public void testCharacterTextSplitter_splitByCharacterCountShorterWordsFirst() {
        // Arrange
        String text = "a a foo bar baz";
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 3, 1);

        // Act
        List<String> result = splitter.splitText(text);

        // Assert
        List<String> expected = new ArrayList<>(Arrays.asList("a a", "foo", "bar", "baz"));

        assertEquals(expected, result);
    }

    @Test
    public void testCharacterTextSplitter_splitByCharactersSplitsNotFoundEasily() {
        // Arrange
        String text = "foo bar baz 123";
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 1, 0);

        // Act
        List<String> result = splitter.splitText(text);

        // Assert
        List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar", "baz", "123"));

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterTextSplitter_invalidArguments() {
        // Arrange
        int chunkSize = 2;
        int chunkOverlap = 4;

        // Act
        new CharacterTextSplitter(null, chunkSize, chunkOverlap);

        // Expect IllegalArgumentException to be thrown
    }

    //TODO, this unit test will need improving. atm it only checks that length of our list of documents, it does not check the contents
    @Test
    public void testCharacterTextSplitter_createDocuments() {
        // Arrange
        List<String> texts = Arrays.asList("foo bar", "baz");
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 3, 0);
        Map<String, String> metadata = new HashMap<>();

        Map<String, String> loc = new HashMap<>();
        loc.put("from", String.valueOf(1));
        loc.put("to", String.valueOf(1));

        // Act
        List<DomainDocument> docs = splitter.createDocuments(texts, Arrays.asList(metadata, metadata));

        // Assert
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", metadata),
                new DomainDocument("bar", metadata),
                new DomainDocument("baz", metadata)
        );

        assertEquals(expectedDocs.size(), docs.size());
    }

    //TODO, this unit test will need improving. atm it only checks that length of our list of documents, it does not check the contents
    @Test
    public void testCharacterTextSplitter_createDocumentsWithMetadata() {
        // Arrange
        List<String> texts = Arrays.asList("foo bar", "baz");
        CharacterTextSplitter splitter = new CharacterTextSplitter(" ", 3, 0);
        List<Map<String, String>> metadataList = Arrays.asList(
                new HashMap<String, String>() {{
                    put("source", "1");
                }},
                new HashMap<String, String>() {{
                    put("source", "2");
                }}
        );

        // Act
        List<DomainDocument> docs = splitter.createDocuments(texts, metadataList);

        // Assert
        List<DomainDocument> expectedDocs = Arrays.asList(
                new DomainDocument("foo", new HashMap<String, String>() {{
                    put("source", "1");
                    put("from", String.valueOf(1));
                    put("to", String.valueOf(1));
                }}),
                new DomainDocument("bar", new HashMap<String, String>() {{
                    put("source", "1");
                    put("from", String.valueOf(1));
                    put("to", String.valueOf(1));
                }}),
                new DomainDocument("baz", new HashMap<String, String>() {{
                    put("source", "2");
                    put("from", String.valueOf(1));
                    put("to", String.valueOf(1));
                }})
        );

        assertEquals(expectedDocs.size(), docs.size());
    }


    @Test
    public void testRecursiveCharacterTextSplitter_iterativeTextSplitter() {
        // Arrange
        String text = "Hi.\n\nI'm Harrison.\n\nHow? Are? You?\nOkay then f f f f.\nThis is a weird text to write, but gotta test the splittingggg some how.\n\nBye!\n\n-H.";
        RecursiveCharacterTextSplitter splitter = new RecursiveCharacterTextSplitter(null, 10, 1);

        // Act
        List<String> output = splitter.splitText(text);

        // Assert
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

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTextSplitter_iterativeTextSplitter_linesLoc() {
        // Arrange
        String text = "Hi.\nI'm Harrison.\n\nHow?\na\nb";
        RecursiveCharacterTextSplitter splitter = new RecursiveCharacterTextSplitter(null, 20, 1);

        // Act
        List<DomainDocument> docs = splitter.createDocuments(Collections.singletonList(text), null);

        // Assert
        DomainDocument doc1 = new DomainDocument("Hi.\nI'm Harrison.", null);
        DomainDocument doc2 = new DomainDocument("How?\na\nb", null);
        List<DomainDocument> expectedDocs = Arrays.asList(doc1, doc2);

        assertEquals(expectedDocs.size(), docs.size());
    }






}
