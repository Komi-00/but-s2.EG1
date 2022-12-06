package examples;

import java.util.HashMap;
import java.util.Set;

import assignment.calculation.Assignment;
import assignment.tutoring.MotivationScale;
import assignment.tutoring.Ressource;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;

public class UseAssignment {
    // Comme une matière pour le moment => une seule note (pour le moment)
    static final String RESSOURCE_NAME = "GRAPHE";

    public static void main(String[] args) {
        HashMap<String, Double> st1Grades = new HashMap<>();
        st1Grades.put(RESSOURCE_NAME, 15.0);
        HashMap<String, Double> st2Grades = new HashMap<>();
        st2Grades.put(RESSOURCE_NAME, 14.0);
        HashMap<String, Double> st3Grades = new HashMap<>();
        st3Grades.put(RESSOURCE_NAME, 16.0);
        HashMap<String, Double> st4Grades = new HashMap<>();
        st4Grades.put(RESSOURCE_NAME, 18.0);
        HashMap<String, Double> st5Grades = new HashMap<>();
        st5Grades.put(RESSOURCE_NAME, 11.0);

        StudentTutor st1 = new StudentTutor("st", "1", st1Grades, 14.0, MotivationScale.HIGH_MOTIVATION, 3);
        StudentTutor st2 = new StudentTutor("st", "2", st2Grades, 13.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        StudentTutor st3 = new StudentTutor("st", "3", st3Grades, 18.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        StudentTutor st4 = new StudentTutor("st", "4", st4Grades, 16.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        StudentTutor st5 = new StudentTutor("st", "5", st5Grades, 10.0, MotivationScale.MEDIUM_MOTIVATION, 2);

        HashMap<String, Double> sid1Grades = new HashMap<>();
        sid1Grades.put(RESSOURCE_NAME, 4.0);
        HashMap<String, Double> sid2Grades = new HashMap<>();
        sid2Grades.put(RESSOURCE_NAME, 6.0);
        HashMap<String, Double> sid3Grades = new HashMap<>();
        sid3Grades.put(RESSOURCE_NAME, 9.0);
        HashMap<String, Double> sid4Grades = new HashMap<>();
        sid4Grades.put(RESSOURCE_NAME, 13.0);
        HashMap<String, Double> sid5Grades = new HashMap<>();
        sid5Grades.put(RESSOURCE_NAME, 10.0);
        HashMap<String, Double> sid6Grades = new HashMap<>();
        sid6Grades.put(RESSOURCE_NAME, 14.0);
        HashMap<String, Double> sid7Grades = new HashMap<>();
        sid7Grades.put(RESSOURCE_NAME, 15.0);

        StudentInDifficulty sid1 = new StudentInDifficulty("sid", "1", sid1Grades, 6.0,
                MotivationScale.HIGH_MOTIVATION);
        StudentInDifficulty sid2 = new StudentInDifficulty("sid", "2", sid2Grades, 8.0,
                MotivationScale.HIGH_MOTIVATION);
        StudentInDifficulty sid3 = new StudentInDifficulty("sid", "3", sid3Grades, 9.0,
                MotivationScale.HIGH_MOTIVATION);
        StudentInDifficulty sid4 = new StudentInDifficulty("sid", "4", sid4Grades, 12.0,
                MotivationScale.MEDIUM_MOTIVATION);
        StudentInDifficulty sid5 = new StudentInDifficulty("sid", "5", sid5Grades, 9.0,
                MotivationScale.LOW_MOTIVATION);
        StudentInDifficulty sid6 = new StudentInDifficulty("sid", "6", sid6Grades, 15.0,
                MotivationScale.LOW_MOTIVATION);
        StudentInDifficulty sid7 = new StudentInDifficulty("sid", "7", sid7Grades, 15.0,
                MotivationScale.HIGH_MOTIVATION);

        Ressource rss = new Ressource(RESSOURCE_NAME, 1000);
        rss.addStudentTutor(st1, st2, st3, st4, st5);
        rss.addStudentInDifficulty(sid1, sid2, sid3, sid4, sid5, sid6, sid7);

        // rss.addAssignment(st4, sid2); //Manual assignment st4 - sid2
        // rss.addForbiddenAssignment(st2, sid4); //Manual assignment st2 - sid4

        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = rss.computeAssignments();

        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            System.out.println(assignment);
        }

    }
}
