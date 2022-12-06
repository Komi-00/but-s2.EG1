package assignment.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import assignment.tutoring.MotivationScale;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;

public class StudentsJSONReader extends JSONReader {
    public StudentsJSONReader(String path) {
        super(path);
    }

    public Set<StudentTutor> readStudentsTutor() throws IOException, IncorrectFileFormatException {
        Set<StudentTutor> studentsTutor = new HashSet<>();
        try {
            studentsTutor.addAll(createStudentsTutorFromJSON(readJSONArray("students_tutor")));
        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }

        return studentsTutor;
    }

    public Set<StudentInDifficulty> readStudentsInDifficulty() throws IOException, IncorrectFileFormatException {
        Set<StudentInDifficulty> studentsInDifficulty = new HashSet<>();
        try {
            studentsInDifficulty.addAll(createStudentsInDifficultyFromJSON(readJSONArray("students_in_difficulty")));
        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }

        return studentsInDifficulty;
    }

    private static Set<StudentTutor> createStudentsTutorFromJSON(JSONArray studentsTutorsJSONArray) {
        Set<StudentTutor> studentsTutors = new HashSet<>();
        for (int i = 0; i < studentsTutorsJSONArray.length(); i++) {
            JSONObject obj = studentsTutorsJSONArray.getJSONObject(i);

            String first = obj.getString("first");
            String last = obj.getString("last");
            Map<String, Double> grades = new HashMap<>();
            grades.put(obj.getString("enrollment").toUpperCase(), obj.getDouble("moy_ressource"));
            double overallGrade = obj.getDouble("moy_generale");
            MotivationScale motivationScale = MotivationScale.valueOf(obj.getString("motivation").toUpperCase());
            int nbAbsences = obj.getInt("nb_absences");
            String enrollment = obj.getString("enrollment").toUpperCase();
            int schoolYear = obj.getInt("year");
            studentsTutors.add(new StudentTutor(first, last, grades, overallGrade, motivationScale, nbAbsences, enrollment, schoolYear));
        }
        return studentsTutors;
    }

    private static Set<StudentInDifficulty> createStudentsInDifficultyFromJSON(JSONArray studentsInDifficultyJSONArray) {
        Set<StudentInDifficulty> studentsInDifficulty = new HashSet<>();

        for (int i = 0; i < studentsInDifficultyJSONArray.length(); i++) {
            JSONObject obj = studentsInDifficultyJSONArray.getJSONObject(i);
            JSONObject gradesObj = obj.getJSONObject("grades");

            String first = obj.getString("first");
            String last = obj.getString("last");

            Map<String, Double> grades = new HashMap<>();
            for (String ressource : gradesObj.keySet()) {
                grades.put(ressource.toUpperCase(), gradesObj.getDouble(ressource));
            }

            double overallGrade = obj.getDouble("moy_generale");
            MotivationScale motivationScale = MotivationScale.valueOf(obj.getString("motivation").toUpperCase());
            int nbAbsences = obj.getInt("nb_absences");

            JSONArray array = obj.getJSONArray("enrollments");
            Set<String> enrollments = new HashSet<>();
            for (int j = 0; j < array.length(); j++) {
                enrollments.add(array.getString(j).toUpperCase());
            }

            studentsInDifficulty.add(new StudentInDifficulty(first, last, grades, overallGrade, motivationScale, nbAbsences, enrollments));
        }
        return studentsInDifficulty;
    }

}
