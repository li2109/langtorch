package ai.knowly.langtorch.prompt.manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {

  public static final TypeAdapterFactory FACTORY =
      new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          Class<T> rawType = (Class<T>) typeToken.getRawType();
          if (rawType != Optional.class) {
            return null;
          }
          final Type[] typeArgs =
              ((ParameterizedType) typeToken.getType()).getActualTypeArguments();
          TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(typeArgs[0]));
          return (TypeAdapter<T>) new OptionalTypeAdapter<>(adapter);
        }
      };
  private final TypeAdapter<T> delegate;

  public OptionalTypeAdapter(TypeAdapter<T> delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<T> read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return Optional.empty();
    }
    T value = delegate.read(in);
    return Optional.ofNullable(value);
  }

  @Override
  public void write(JsonWriter out, Optional<T> value) throws IOException {
    if (value.isPresent()) {
      delegate.write(out, value.get());
    } else {
      out.nullValue();
    }
  }
}
