package assignment.output;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;

public class AssignmentsJSONWriter extends JSONWriter {
    public AssignmentsJSONWriter(String path) {
        super(path);
    }

    public void writeAssignments(Collection<Assignment<StudentTutor, StudentInDifficulty>> assignments) throws IOException {
        JSONObject assignmentsCategoriesJSONObject = new JSONObject();

        for (AssignmentType type : AssignmentType.values()) {
            assignmentsCategoriesJSONObject.put(type.toString(), new JSONArray());
        }

        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            AssignmentType assignmentType = assignment.getType();
            String assignmentTypeName = assignmentType.toString();

            JSONArray assignmentsJSONArray = assignmentsCategoriesJSONObject.getJSONArray(assignmentTypeName);
            JSONObject assignmentJSONObject = createAssignmentJSONObject(assignment);

            assignmentsJSONArray.put(assignmentJSONObject);
        }

        this.writeJSONObject(assignmentsCategoriesJSONObject);
    }

    private static JSONObject createAssignmentJSONObject(Assignment<StudentTutor, StudentInDifficulty> assignment) {
        JSONObject assignmentJSONObject = new JSONObject();

        StudentTutor studentTutor = assignment.getLeft();
        StudentInDifficulty studentInDifficulty = assignment.getRight();

        assignmentJSONObject.put("student_tutor", studentTutor.getFullName());
        assignmentJSONObject.put("student_in_difficulty", studentInDifficulty.getFullName());
        assignmentJSONObject.put("score", assignment.getScore());
        return assignmentJSONObject;
    }

}
