package assignment.input;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONReader {
    private final String PATH;
    private JSONObject OBJ;

    protected JSONReader(String path) {
        this.PATH = path;
        this.OBJ = null;
    }

    public String getPath() {
        return this.PATH;
    }

    private String readFile() throws IOException {
        StringBuilder data = new StringBuilder();
        try (Scanner sc = new Scanner(new File(this.PATH))) {
            while (sc.hasNextLine()) {
                data.append(sc.nextLine());
            }
        }
        return data.toString();
    }

    public JSONObject readJSONObject() throws IOException, JSONException {
        if (this.OBJ == null) {
            this.OBJ = new JSONObject(this.readFile());
        }
        return this.OBJ;
    }

    public JSONObject readJSONObject(String key) throws IOException, JSONException {
        return this.readJSONObject().getJSONObject(key);
    }

    public JSONArray readJSONArray(String key) throws IOException, JSONException {
        return this.readJSONObject().getJSONArray(key);
    }

}
