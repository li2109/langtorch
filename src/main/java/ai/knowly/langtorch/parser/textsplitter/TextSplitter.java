package ai.knowly.langtorch.parser.textsplitter;

import ai.knowly.langtorch.schema.io.DomainDocument;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TextSplitter {

    public int chunkSize;

    public int chunkOverlap;

    public TextSplitter(int chunkSize, int chunkOverlap) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        if (this.chunkOverlap >= this.chunkSize) {
            throw new IllegalArgumentException("chunkOverlap cannot be equal to or greater than chunkSize");
        }
    }

    abstract public List<String> splitText(String text);

    public List<DomainDocument> createDocuments(List<String> texts, @Nullable List<Map<String, String>> metaDatas) {
        List<Map<String, String>> _metadatas;

        if (metaDatas != null) {
            _metadatas = metaDatas.size() > 0 ? metaDatas : new ArrayList<>();
        } else {
            _metadatas = new ArrayList<>();
        }
        ArrayList<DomainDocument> documents = new ArrayList<>();

        for (int i = 0; i < texts.size(); i += 1) {
            String text = texts.get(i);
            int lineCounterIndex = 1;
            String prevChunk = null;

            for (String chunk : splitText(text)) {
                int numberOfIntermediateNewLines = 0;
                if (prevChunk != null) {
                    int indexChunk = text.indexOf(chunk);
                    int indexEndPrevChunk = text.indexOf(prevChunk) + prevChunk.length();
                    String removedNewlinesFromSplittingText = text.substring(indexChunk, indexEndPrevChunk);
                    numberOfIntermediateNewLines = removedNewlinesFromSplittingText.split("\n").length - 1;
                }
                lineCounterIndex += numberOfIntermediateNewLines;
                int newLinesCount = chunk.split("\n").length - 1;

                Map<String, String> loc;
                //todo should we also check what type of object is "loc"?
                if (_metadatas.get(i) != null) {
                    if (!_metadatas.get(i).isEmpty() && _metadatas.get(i).get("loc") != null) {
                        loc = new HashMap<>(_metadatas.get(i));
                    } else {
                        loc = new HashMap<>();
                    }
                } else {
                    loc = new HashMap<>();
                }

                loc.put("from", String.valueOf(lineCounterIndex));
                loc.put("to", String.valueOf(lineCounterIndex + newLinesCount));

                Map<String, String> metadataWithLinesNumber = new HashMap<>();
                if (_metadatas.get(i) != null) {
                    metadataWithLinesNumber.putAll(_metadatas.get(i));
                }
                metadataWithLinesNumber.putAll(loc);

                documents.add(new DomainDocument(chunk, metadataWithLinesNumber));
                lineCounterIndex += newLinesCount;
                prevChunk = chunk;
            }
        }
        return documents;
    }

    public List<DomainDocument> splitDocuments(List<DomainDocument> documents) {

        List<DomainDocument> selectedDocs = documents.stream().filter(doc -> doc.getPageContent() != null).collect(Collectors.toList());

        List<String> texts = selectedDocs.stream().map(DomainDocument::getPageContent).collect(Collectors.toList());
        List<Map<String, String>> metaDatas = selectedDocs.stream().map(DomainDocument::getMetadata).collect(Collectors.toList());

        return this.createDocuments(texts, metaDatas);
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
            int _len = d.length();

            if (total + _len + (currentDoc.size() > 0 ? separator.length() : 0) > this.chunkSize) {
                if (total > this.chunkSize) {
                    System.out.println("Created a chunk of size " + total + ", which is longer than the specified " + this.chunkSize);
                }

                if (currentDoc.size() > 0) {
                    String doc = joinDocs(currentDoc, separator);
                    if (doc != null) {
                        docs.add(doc);
                    }

                    while (total > this.chunkOverlap || (total + _len > this.chunkSize && total > 0)) {
                        total -= currentDoc.get(0).length();
                        currentDoc.remove(0);
                    }
                }
            }

            currentDoc.add(d);
            total += _len;
        }

        String doc = joinDocs(currentDoc, separator);
        if (doc != null) {
            docs.add(doc);
        }

        return docs;
    }

}
