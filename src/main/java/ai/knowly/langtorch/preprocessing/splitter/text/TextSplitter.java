package ai.knowly.langtorch.preprocessing.splitter.text;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The TextSplitter class provides functionality for splitting text into chunks.
 */
public abstract class TextSplitter {

    public final int chunkSize;

    public final int chunkOverlap;

    public TextSplitter(int chunkSize, int chunkOverlap) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        if (this.chunkOverlap >= this.chunkSize) {
            throw new IllegalArgumentException("chunkOverlap cannot be equal to or greater than chunkSize");
        }
    }

    abstract public List<String> splitText(String text);

    public List<DomainDocument> createDocuments(List<String> texts, Optional<List<Metadata>> docMetadatas) {
        List<Metadata> metadatas =
                (docMetadatas.isPresent() && docMetadatas.get().size() > 0) ?
                        docMetadatas.get() : Collections.nCopies(texts.size(), Metadata.createEmpty());

        ArrayList<DomainDocument> documents = new ArrayList<>();

        for (int i = 0; i < texts.size(); i += 1) {
            String text = texts.get(i);
            int lineCounterIndex = 1;
            String prevChunk = null;

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
                if (metadatas.get(i).getValue().containsKey("loc")) {
                    loc = metadatas.get(i).getValue();
                } else {
                    loc = new MultiKeyMap<>();
                }

                loc.put("loc", "from", String.valueOf(lineCounterIndex));
                loc.put("loc", "to", String.valueOf(lineCounterIndex + newLinesCount));

                Metadata metadataWithLinesNumber = Metadata.createEmpty();
                if (metadatas.get(i) != null && !metadatas.get(i).getValue().isEmpty()) {
                    metadataWithLinesNumber.getValue().putAll(metadatas.get(i).getValue());
                }
                metadataWithLinesNumber.getValue().putAll(loc);

                documents.add(new DomainDocument(chunk, Optional.of(metadataWithLinesNumber)));
                lineCounterIndex += newLinesCount;
                prevChunk = chunk;
            }
        }
        return documents;
    }

    public List<DomainDocument> splitDocuments(List<DomainDocument> documents) {

        List<DomainDocument> selectedDocs = documents.stream().filter(doc -> doc.getPageContent() != null).collect(Collectors.toList());

        List<String> texts = selectedDocs.stream().map(DomainDocument::getPageContent).collect(Collectors.toList());
        List<Metadata> metadatas =
                selectedDocs.stream().map(doc -> doc.getMetadata().isPresent() ?
                        doc.getMetadata().get() : Metadata.createEmpty()).collect(Collectors.toList());

        return this.createDocuments(texts, Optional.of(metadatas));
    }

    @Nullable
    private String joinDocs(List<String> docs, String separator) {
        String text = String.join(separator, docs);
        return text.equals("") ? null : text;
    }

    public List<String> mergeSplits(List<String> splits, String separator) {
        List<String> docs = new ArrayList<>();
        List<String> currentDoc = new ArrayList<>();
        int total = 0;

        for (String d : splits) {
            int length = d.length();

            if (total + length + (currentDoc.size() > 0 ? separator.length() : 0) > this.chunkSize) {
                if (total > this.chunkSize) {
                    System.out.println("Created a chunk of size " + total + ", which is longer than the specified " + this.chunkSize);
                }

                if (currentDoc.size() > 0) {
                    String doc = joinDocs(currentDoc, separator);
                    if (doc != null) {
                        docs.add(doc);
                    }

                    while (total > this.chunkOverlap || (total + length > this.chunkSize && total > 0)) {
                        total -= currentDoc.get(0).length();
                        currentDoc.remove(0);
                    }
                }
            }

            currentDoc.add(d);
            total += length;
        }

        String doc = joinDocs(currentDoc, separator);
        if (doc != null) {
            docs.add(doc);
        }

        return docs;
    }

}
