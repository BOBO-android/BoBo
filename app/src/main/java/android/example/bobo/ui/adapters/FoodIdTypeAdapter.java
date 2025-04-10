package android.example.bobo.ui.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FoodIdTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        if (token == JsonToken.STRING) {
            return in.nextString();
        } else if (token == JsonToken.BEGIN_OBJECT) {
            // Đọc object và tìm trường "_id" hoặc "id"
            in.beginObject();
            String fieldName;
            String id = null;

            while (in.hasNext()) {
                fieldName = in.nextName();
                if (fieldName.equals("_id") || fieldName.equals("id")) {
                    id = in.nextString();
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
            return id;
        } else {
            in.skipValue();
            return null;
        }
    }
}
