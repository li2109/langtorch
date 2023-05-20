package ai.knowly.langtorch.prompt.manager;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromptTemplateTypeAdapter extends TypeAdapter<PromptTemplate> {

  @Override
  public void write(JsonWriter out, PromptTemplate promptTemplate) throws IOException {
    out.beginObject();
    out.name("template").value(promptTemplate.template().orElse(null));
    out.name("exampleHeader").value(promptTemplate.exampleHeader().orElse(null));
    out.name("examples").beginArray();
    for (String example : promptTemplate.examples()) {
      out.value(example);
    }
    out.endArray();

    out.name("variables").beginObject();
    for (Map.Entry<String, String> entry : promptTemplate.variables().entrySet()) {
      out.name(entry.getKey()).value(entry.getValue());
    }
    out.endObject();
    out.endObject();
  }

  @Override
  public PromptTemplate read(JsonReader in) throws IOException {
    PromptTemplate.Builder builder = PromptTemplate.builder();
    in.beginObject();
    while (in.hasNext()) {
      String name = in.nextName();
      switch (name) {
        case "template":
          builder.setTemplate(in.nextString());
          break;
        case "exampleHeader":
          builder.setExampleHeader(in.nextString());
          break;
        case "examples":
          in.beginArray();
          List<String> examples = new ArrayList<>();
          while (in.hasNext()) {
            examples.add(in.nextString());
          }
          in.endArray();
          builder.setExamples(examples);
          break;
        case "variables":
          in.beginObject();
          Map<String, String> variables = new HashMap<>();
          while (in.hasNext()) {
            String variableName = in.nextName();
            String variableValue = in.nextString();
            variables.put(variableName, variableValue);
          }
          in.endObject();
          builder.addAllVariableValuePairs(variables);
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
