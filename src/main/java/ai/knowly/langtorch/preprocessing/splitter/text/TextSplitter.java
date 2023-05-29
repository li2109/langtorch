package ai.knowly.langtorch.preprocessing.splitter.text;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The TextSplitter class provides functionality for splitting text into chunks.
 */
public abstract class TextSplitter {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    /**
     * The amount of words inside one chunk
     */
    public final int wordCount;

    /**
     * amount of words from previous chunk, it will be empty for the first chunk
     */
    public final int wordOverlap;

    protected TextSplitter(int wordCount, int wordOverlap) {
        this.wordCount = wordCount;
        this.wordOverlap = wordOverlap;
        if (this.wordOverlap >= this.wordCount) {
            throw new IllegalArgumentException("chunkOverlap cannot be equal to or greater than chunkSize");
        }
    }

    public abstract List<String> splitText(String text);

    public List<DomainDocument> createDocumentsSplitFromSingle(DomainDocument document) {
        String text = document.getPageContent();
        Metadata metadata = document.getMetadata().orElse(Metadata.create());
        ArrayList<DomainDocument> docsToReturn = new ArrayList<>();

        addDocumentFromWordChunk(docsToReturn, text, metadata);
        return docsToReturn;
    }

    public List<DomainDocument> createDocumentsSplitFromList(List<DomainDocument> documents) {

        ArrayList<DomainDocument> docsToReturn = new ArrayList<>();

        for (DomainDocument document : documents) {
            String text = document.getPageContent();
            Metadata metadata = document.getMetadata().orElse(Metadata.create());
            addDocumentFromWordChunk(docsToReturn, text, metadata);
        }
        return docsToReturn;
    }

    private void addDocumentFromWordChunk(
            ArrayList<DomainDocument> docsToReturn,
            String text,
            Metadata metadata) {
        String prevChunk = null;
        int lineCounterIndex = 1;

        for (String chunk : splitText(text)) {
            int numberOfIntermediateNewLines = 0;
            if (prevChunk != null) {
                int indexChunk = StringUtils.indexOf(text, chunk);
                int indexEndPrevChunk = StringUtils.indexOf(text, prevChunk) + prevChunk.length();
                String removedNewlinesFromSplittingText = StringUtils.substring(text, indexChunk, indexEndPrevChunk);
                numberOfIntermediateNewLines = StringUtils.countMatches(removedNewlinesFromSplittingText, "\n");
            }
            lineCounterIndex += numberOfIntermediateNewLines;
            int newLinesCount = StringUtils.countMatches(chunk, "\n");

            MultiKeyMap<String, String> loc;
            if (metadata.getValue().containsKey("loc", "")) {
                loc = metadata.getValue();
            } else {
                loc = new MultiKeyMap<>();
            }

            loc.put("loc", "from", String.valueOf(lineCounterIndex));
            loc.put("loc", "to", String.valueOf(lineCounterIndex + newLinesCount));

            Metadata metadataWithLinesNumber = Metadata.create();
            if (!metadata.getValue().isEmpty()) {
                metadataWithLinesNumber.getValue().putAll(metadata.getValue());
            }
            metadataWithLinesNumber.getValue().putAll(loc);

            docsToReturn.add(new DomainDocument(chunk, Optional.of(metadataWithLinesNumber)));
            lineCounterIndex += newLinesCount;
            prevChunk = chunk;
        }
    }

    public List<DomainDocument> splitWordsFromDocuments(List<DomainDocument> documents) {
        List<DomainDocument> selectedDocs = documents.stream().filter(doc -> doc.getPageContent() != null).collect(Collectors.toList());
        return this.createDocumentsSplitFromList(selectedDocs);
    }

    private Optional<String> joinDocs(List<String> docs, String separator) {
        String text = String.join(separator, docs);
        return text.equals("") ? Optional.empty() : Optional.of(text);
    }

    public List<String> mergeSplits(List<String> splits, String separator) {
        List<String> docs = new ArrayList<>();
        List<String> currentDoc = new ArrayList<>();
        int total = 0;

        for (String d : splits) {
            int length = d.length();

            if (total + length + (currentDoc.size() > 0 ? separator.length() : 0) > this.wordCount) {
                if (total > this.wordCount) {
                    logger.atInfo().log("Created a chunk of size " + total + ", which is longer than the specified " + this.wordCount);
                }

                if (currentDoc.size() > 0) {
                    joinDocs(currentDoc, separator).ifPresent(docs::add);

                    while (total > this.wordOverlap || (total + length > this.wordCount && total > 0)) {
                        total -= currentDoc.get(0).length();
                        currentDoc.remove(0);
                    }
                }
            }

            currentDoc.add(d);
            total += length;
        }

        joinDocs(currentDoc, separator).ifPresent(docs::add);
        return docs;
    }

}
