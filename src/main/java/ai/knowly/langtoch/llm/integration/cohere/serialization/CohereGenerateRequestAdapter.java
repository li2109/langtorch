package ai.knowly.langtoch.llm.integration.cohere.serialization;

import ai.knowly.langtoch.llm.integration.cohere.schema.CohereGenerateRequest;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohereGenerateRequestAdapter extends TypeAdapter<CohereGenerateRequest> {
  @Override
  public void write(JsonWriter out, CohereGenerateRequest value) throws IOException {
    out.beginObject();
    out.name("prompt").value(value.prompt());
    out.name("model").value(value.model());
    out.name("numGenerations").value(value.numGenerations());
    out.name("maxTokens").value(value.maxTokens());
    if (value.preset() != null && !value.preset().isBlank()) {
      out.name("preset").value(value.preset());
    }
    out.name("temperature").value(value.temperature());
    out.name("k").value(value.k());
    out.name("p").value(value.p());
    out.name("frequencyPenalty").value(value.frequencyPenalty());
    out.name("presencePenalty").value(value.presencePenalty());
    if (value.endSequences() != null && !value.endSequences().isEmpty()) {
      out.name("endSequences").beginArray();
      for (String endSequence : value.endSequences()) {
        out.value(endSequence);
      }
      out.endArray();
    }
    if (value.stopSequences() != null && !value.stopSequences().isEmpty()) {
      out.name("stopSequences").beginArray();
      for (String stopSequence : value.stopSequences()) {
        out.value(stopSequence);
      }
      out.endArray();
    }
    out.name("returnLikelihoods").value(value.returnLikelihoods());
    if (value.logitBias() != null && !value.logitBias().entrySet().isEmpty()) {
      out.name("logitBias").beginObject();
      for (Map.Entry<String, Float> entry : value.logitBias().entrySet()) {
        out.name(entry.getKey()).value(entry.getValue());
      }
      out.endObject();
    }
    out.name("truncate").value(value.truncate());
    out.endObject();
  }

  @Override
  public CohereGenerateRequest read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    in.beginObject();
    CohereGenerateRequest.Builder builder = CohereGenerateRequest.builder();
    while (in.hasNext()) {
      String name = in.nextName();
      switch (name) {
        case "prompt":
          builder.prompt(in.nextString());
          break;
        case "model":
          builder.model(in.nextString());
          break;
        case "numGenerations":
          builder.numGenerations(in.nextInt());
          break;
        case "maxTokens":
          builder.maxTokens(in.nextInt());
          break;
        case "preset":
          builder.preset(in.nextString());
          break;
        case "temperature":
          builder.temperature(in.nextDouble());
          break;
        case "k":
          builder.k(in.nextInt());
          break;
        case "p":
          builder.p(in.nextDouble());
          break;
        case "frequencyPenalty":
          builder.frequencyPenalty(in.nextDouble());
          break;
        case "presencePenalty":
          builder.presencePenalty(in.nextDouble());
          break;
        case "endSequences":
          List<String> endSequences = new ArrayList<>();
          in.beginArray();
          while (in.hasNext()) {
            endSequences.add(in.nextString());
          }
          in.endArray();
          builder.endSequences(endSequences);
          break;
        case "stopSequences":
          List<String> stopSequences = new ArrayList<>();
          in.beginArray();
          while (in.hasNext()) {
            stopSequences.add(in.nextString());
          }
          in.endArray();
          builder.stopSequences(stopSequences);
          break;
        case "returnLikelihoods":
          builder.returnLikelihoods(in.nextString());
          break;
        case "logitBias":
          Map<String, Float> logitBias = new HashMap<>();
          in.beginObject();
          while (in.hasNext()) {
            logitBias.put(in.nextName(), (float) in.nextDouble());
          }
          in.endObject();
          builder.logitBias(logitBias);
          break;
        case "truncate":
          builder.truncate(in.nextString());
          break;
        default:
          in.skipValue();
          break;
      }
    }
    in.endObject();
    return builder.build();
  }
}
