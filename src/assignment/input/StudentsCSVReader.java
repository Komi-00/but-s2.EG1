package assignment.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import assignment.tutoring.MotivationScale;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;

public class StudentsCSVReader {

    private final String path;

    public StudentsCSVReader(String path) {
        this.path = path;
    }

    public Set<StudentInDifficulty> readStudentsInDifficulty() throws IOException, IncorrectFileFormatException {
        Set<StudentInDifficulty> studentsInDifficulty = new HashSet<>();
        String line;
        final String DELIMITER = ";";

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            int cpt = 0;
            List<String> columnNameRessources = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] contents = line.split(DELIMITER);
                if (cpt == 0) {
                    columnNameRessources = Arrays.asList(contents).subList(5, contents.length);
                } else {

                    studentsInDifficulty.add(createStudentFromCsvLine(contents, columnNameRessources));
                }
                cpt++;
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IncorrectFileFormatException(this.path);
        }

        return studentsInDifficulty;
    }

    private static StudentInDifficulty createStudentFromCsvLine(String[] contents, List<String> ressources) {
        String firstName = contents[0];
        String lastName = contents[1];
        double overallGrade = Double.parseDouble(contents[2]);
        MotivationScale motivation = MotivationScale.valueOf(contents[3].toUpperCase());
        int nbAbsences = Integer.parseInt(contents[4]);
        Set<String> enrollments = new HashSet<>();
        Map<String, Double> grades = new HashMap<>();
        for (int i = 5; i < contents.length; i++) {
            String columnValue = contents[i];
            if (!columnValue.isEmpty()) {
                String ressource = ressources.get(i - 5);
                enrollments.add(ressource.toUpperCase());
                grades.put(ressource.toUpperCase(), Double.parseDouble(columnValue));
            }
        }
        return new StudentInDifficulty(firstName, lastName, grades, overallGrade, motivation, nbAbsences, enrollments);
    }

    public Set<StudentTutor> readStudentsTutor() throws IOException, IncorrectFileFormatException {
        Set<StudentTutor> studentsTutors = new HashSet<>();
        String line;
        final String DELIMITER = ";";

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            int cpt = 0;
            while ((line = reader.readLine()) != null) {
                String[] contents = line.split(DELIMITER);
                if (cpt > 0) {
                    studentsTutors.add(createStudentFromCsvLine(contents));
                }
                cpt++;
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IncorrectFileFormatException(this.path);
        }

        return studentsTutors;
    }

    private static StudentTutor createStudentFromCsvLine(String[] stContents) {
        String firstName = stContents[0];
        String lastName = stContents[1];
        Map<String, Double> grades = new HashMap<>();
        grades.put(stContents[2].toUpperCase(), Double.parseDouble(stContents[3]));
        double overallGrade = Double.parseDouble(stContents[4]);
        MotivationScale motivation = MotivationScale.valueOf(stContents[5].toUpperCase());
        int nbAbsences = Integer.parseInt(stContents[6]);
        String enrollment = stContents[2].toUpperCase();
        int schoolYear = Integer.parseInt(stContents[7]);
        return new StudentTutor(firstName, lastName, grades, overallGrade, motivation, nbAbsences, enrollment, schoolYear);
    }
}