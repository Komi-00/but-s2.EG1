package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;
import assignment.tutoring.MotivationScale;
import assignment.tutoring.Ressource;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentNotFoundException;
import assignment.tutoring.StudentTutor;

public class AssignmentAlgoTest {
    private StudentTutor st1;
    private StudentTutor st2;
    private StudentTutor st3;
    private StudentTutor st4;
    private StudentTutor st5;

    private StudentInDifficulty sid1;
    private StudentInDifficulty sid2;
    private StudentInDifficulty sid3;
    private StudentInDifficulty sid4;
    private StudentInDifficulty sid5;
    private StudentInDifficulty sid6;
    private StudentInDifficulty sid7;

    private Ressource ressource;

    @BeforeEach
    public void setup() {
        HashMap<String, Double> st1Grades = new HashMap<>();
        String RESSOURCE_NAME = "GRAPHE";
        st1Grades.put(RESSOURCE_NAME, 15.0);
        HashMap<String, Double> st2Grades = new HashMap<>();
        st2Grades.put(RESSOURCE_NAME, 14.0);
        HashMap<String, Double> st3Grades = new HashMap<>();
        st3Grades.put(RESSOURCE_NAME, 16.0);
        HashMap<String, Double> st4Grades = new HashMap<>();
        st4Grades.put(RESSOURCE_NAME, 18.0);
        HashMap<String, Double> st5Grades = new HashMap<>();
        st5Grades.put(RESSOURCE_NAME, 11.0);

        st1 = new StudentTutor("st", "1", st1Grades, 14.0, MotivationScale.HIGH_MOTIVATION, 3);
        st2 = new StudentTutor("st", "2", st2Grades, 13.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        st3 = new StudentTutor("st", "3", st3Grades, 18.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        st4 = new StudentTutor("st", "4", st4Grades, 16.0, MotivationScale.MEDIUM_MOTIVATION, 2);
        st5 = new StudentTutor("st", "5", st5Grades, 10.0, MotivationScale.MEDIUM_MOTIVATION, 2);

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

        sid1 = new StudentInDifficulty("sid", "1", sid1Grades, 6.0, MotivationScale.HIGH_MOTIVATION);
        sid2 = new StudentInDifficulty("sid", "2", sid2Grades, 8.0, MotivationScale.HIGH_MOTIVATION);
        sid3 = new StudentInDifficulty("sid", "3", sid3Grades, 9.0, MotivationScale.HIGH_MOTIVATION);
        sid4 = new StudentInDifficulty("sid", "4", sid4Grades, 12.0, MotivationScale.MEDIUM_MOTIVATION);
        sid5 = new StudentInDifficulty("sid", "5", sid5Grades, 9.0, MotivationScale.LOW_MOTIVATION);
        sid6 = new StudentInDifficulty("sid", "6", sid6Grades, 15.0, MotivationScale.LOW_MOTIVATION);
        sid7 = new StudentInDifficulty("sid", "7", sid7Grades, 15.0, MotivationScale.HIGH_MOTIVATION);

        ressource = new Ressource(RESSOURCE_NAME, 1000);
        ressource.addStudentTutor(st1, st2, st3, st4, st5);
        ressource.addStudentInDifficulty(sid1, sid2, sid3, sid4, sid5, sid6, sid7);
    }

    @Test
    public void classicAssignment() {
        Set<Assignment<StudentTutor, StudentInDifficulty>> expectedAssignments = new HashSet<>();
        expectedAssignments.add(new Assignment<>(st1, sid1));
        expectedAssignments.add(new Assignment<>(st1, sid2));
        expectedAssignments.add(new Assignment<>(st5, sid7));
        expectedAssignments.add(new Assignment<>(st2, sid4));
        expectedAssignments.add(new Assignment<>(st4, sid3));
        expectedAssignments.add(new Assignment<>(st3, sid5));

        // Todo: try to avoid duplication
        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = ressource.computeAssignments();

        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            assertTrue(expectedAssignments.contains(assignment));
        }
        // To verify that we don't have more assignments (and therefore bad) than
        // expected
        assertEquals(expectedAssignments.size(), assignments.size());
    }

    @Test
    public void forcedAssignment() throws StudentNotFoundException {
        Set<Assignment<StudentTutor, StudentInDifficulty>> expectedAssignments = new HashSet<>();
        expectedAssignments.add(new Assignment<>(st1, sid1));
        expectedAssignments.add(new Assignment<>(st1, sid3));
        expectedAssignments.add(new Assignment<>(st5, sid7));
        expectedAssignments.add(new Assignment<>(st2, sid4));
        expectedAssignments.add(new Assignment<>(st4, sid2));
        expectedAssignments.add(new Assignment<>(st3, sid5));

        ressource.addAssignment(st4, sid2, AssignmentType.FORCED);

        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = ressource.computeAssignments();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            assertTrue(expectedAssignments.contains(assignment));
        }
        // To verify that we don't have more assignments (and therefore bad) than
        // expected
        assertEquals(expectedAssignments.size(), assignments.size());
    }

    @Test
    public void forbiddenAssignment() throws StudentNotFoundException {
        Set<Assignment<StudentTutor, StudentInDifficulty>> expectedAssignments = new HashSet<>();
        expectedAssignments.add(new Assignment<>(st1, sid1));
        expectedAssignments.add(new Assignment<>(st1, sid2));
        expectedAssignments.add(new Assignment<>(st5, sid7));
        expectedAssignments.add(new Assignment<>(st2, sid5));
        expectedAssignments.add(new Assignment<>(st4, sid3));
        expectedAssignments.add(new Assignment<>(st3, sid4));

        ressource.addAssignment(st2, sid4, AssignmentType.FORBIDDEN);

        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = ressource.computeAssignments();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            assertTrue(expectedAssignments.contains(assignment));
        }
        // To verify that we don't have more assignments (and therefore bad) than
        // expected
        assertEquals(expectedAssignments.size(), assignments.size());
    }

    @Ignore
    public void weighingAssignmentCriteria() {

    }

}