package assignment.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;

public class AssignmentsJSONReader extends JSONReader {

    public AssignmentsJSONReader(String path) {
        super(path);
    }

    public List<Assignment<String, String>> readNamesAssignments() throws IOException, IncorrectFileFormatException {
        List<Assignment<String, String>> assignments = new ArrayList<>();
        try {
            JSONObject assignmentsJSONObject = this.readJSONObject();
            for (String assignmentType : assignmentsJSONObject.keySet()) {
                JSONArray assignmentsJSONArray = assignmentsJSONObject.getJSONArray(assignmentType);
                assignments.addAll(createAssignmentsFromJSONArray(assignmentsJSONArray, assignmentType));
            }
        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }
        return assignments;

    }

    private static List<Assignment<String, String>> createAssignmentsFromJSONArray(JSONArray arr, String assignmentType) {
        List<Assignment<String, String>> assignments = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            assignments.add(createAssignmentFromJSONObject(arr.getJSONObject(i), assignmentType.toUpperCase()));
        }
        return assignments;
    }

    private static Assignment<String, String> createAssignmentFromJSONObject(JSONObject obj, String assignmentType) {
        if (obj.has("score")) {
            return new Assignment<>(obj.getString("student_tutor"), obj.getString("student_in_difficulty"),
                    AssignmentType.valueOf(assignmentType), obj.getDouble("score"));
        }
        return new Assignment<>(obj.getString("student_tutor"), obj.getString("student_in_difficulty"),
                AssignmentType.valueOf(assignmentType));

    }

}
