package assignment.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public abstract class JSONWriter {
    private static final int INDENT_FACTOR = 4;
    private final String path;

    protected JSONWriter(String path) {
        this.path = path;
    }

    public void writeJSONObject(JSONObject obj) throws IOException {
        File file = new File(this.path);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            obj.write(fileWriter, INDENT_FACTOR, 0);
        }
    }

}
