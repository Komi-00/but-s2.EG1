package tests;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import assignment.AssignmentPlatform;
import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;
import assignment.calculation.ScoreCalculator;
import assignment.calculation.ScoreCriteria;
import assignment.tutoring.CriteriaFilter;
import assignment.tutoring.FilterCriteriaType;
import assignment.tutoring.MotivationScale;
import assignment.tutoring.NotAllowedException;
import assignment.tutoring.RessourceNotFoundException;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentNotFoundException;
import assignment.tutoring.StudentTutor;
import assignment.tutoring.Teacher;

public class AssignmentProgramTest {
    private Teacher t1;
    private Set<String> skills;

    private AssignmentPlatform assignmentPlatform;

    private StudentTutor st1;
    private StudentTutor st2;
    private StudentTutor st3;
    private StudentTutor st4;

    private StudentInDifficulty sid1;
    private StudentInDifficulty sid2;

    @BeforeEach
    public void setup() {
        skills = new HashSet<>();
        skills.add("IHM");
        skills.add("POO");
        t1 = new Teacher("Samuel", "Kopa", skills);

        Map<String, Integer> ressources = new HashMap<>();
        ressources.put("IHM", 20);
        ressources.put("POO", 20);
        ressources.put("GRAPHE", 20);
        assignmentPlatform = new AssignmentPlatform(t1, ressources);

        String st1Enrollment = "POO";
        String st2Enrollment = "IHM";
        HashMap<String, Double> st1Grades = new HashMap<>();
        HashMap<String, Double> st2Grades = new HashMap<>();
        st1Grades.put(st1Enrollment, 17.5);
        st2Grades.put(st2Enrollment, 16.2);
        st1 = new StudentTutor("Florian", "Etrillard", st1Grades, 17.1, MotivationScale.HIGH_MOTIVATION, 5, st1Enrollment, 2);
        st2 = new StudentTutor("Felix", "Pereira", st2Grades, 17.2, MotivationScale.HIGH_MOTIVATION, 1, st2Enrollment, 2);

        HashMap<String, Double> sid1Grades = new HashMap<>();
        sid1Grades.put("POO", 10.1);
        sid1Grades.put("IHM", 8.7);
        sid1Grades.put("GRAPHE", 10.7);
        Set<String> sid1Enrollments = new HashSet<>();
        sid1Enrollments.add("POO");
        sid1Enrollments.add("IHM");
        sid1Enrollments.add("GRAPHE");
        sid1 = new StudentInDifficulty("Renan", "Declercq", sid1Grades, 11.6, MotivationScale.HIGH_MOTIVATION, 1, sid1Enrollments);

        assignmentPlatform.addStudentTutor(st1);
        assignmentPlatform.addStudentTutor(st2);
        assignmentPlatform.addStudentInDifficulty(sid1);

        HashMap<String, Double> st3Grades = new HashMap<>();
        String st3Enrollment = "POO";
        st3Grades.put(st3Enrollment, 16.3);
        st3 = new StudentTutor("Jeanne", "Dupont", st3Grades, 16.2, MotivationScale.MEDIUM_MOTIVATION, 3, st3Enrollment, 3);

        HashMap<String, Double> sid2Grades = new HashMap<>();
        Set<String> sid2Enrollments = new HashSet<>();
        sid2Enrollments.add("POO");
        sid2Enrollments.add("GRAPHE");
        sid2Grades.put("POO", 7.3);
        sid2Grades.put("GRAPHE", 6.8);
        sid2 = new StudentInDifficulty("Lionel", "Smith", sid2Grades, 10.3, MotivationScale.LOW_MOTIVATION, 4, sid2Enrollments);
    }

    @Test
    public void getStudent() throws StudentNotFoundException {
        assertSame(st1, assignmentPlatform.getStudentTutorByName("Florian", "Etrillard"));
        assertSame(sid1, assignmentPlatform.getStudentInDifficultyByName("Renan", "Declercq"));
    }

    @Test
    public void getStudentNotFound() {
        try {
            assignmentPlatform.getStudentTutorByName("Kevin", "Unknown");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }

        try {
            assignmentPlatform.getStudentInDifficultyByName("Kevin", "Unknown");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void addStudents() throws StudentNotFoundException {
        try {
            assignmentPlatform.getStudentTutorByName("Jeanne", "Dupont");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }

        try {
            assignmentPlatform.getStudentInDifficultyByName("Lionel", "Smith");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }

        assignmentPlatform.addStudentTutor(st3);
        assignmentPlatform.addStudentInDifficulty(sid2);

        assertSame(st3, assignmentPlatform.getStudentTutorByName("Jeanne", "Dupont"));
        assertSame(sid2, assignmentPlatform.getStudentInDifficultyByName("Lionel", "Smith"));
    }

    @Test
    public void removeStudents() throws StudentNotFoundException {
        assertSame(st1, assignmentPlatform.getStudentTutorByName("Florian", "Etrillard"));
        assertSame(sid1, assignmentPlatform.getStudentInDifficultyByName("Renan", "Declercq"));

        assignmentPlatform.removeStudentTutor(st1);
        assignmentPlatform.removeStudentInDifficulty(sid1);

        try {
            assignmentPlatform.getStudentTutorByName("Florian", "Etrillard");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }

        try {
            assignmentPlatform.getStudentInDifficultyByName("Renan", "Declercq");
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void setConnectedTeacher() {
        assertSame(t1, assignmentPlatform.getConnectedTeacher());

        skills = new HashSet<>();
        skills.add("GRAPHE");
        Teacher t2 = new Teacher("Brigitte", "Saumon", skills);
        assignmentPlatform.setConnectedTeacher(t2);

        assertSame(t2, assignmentPlatform.getConnectedTeacher());
    }

    @Test
    public void handleCriteriaCoefficients() {
        Map<ScoreCriteria, Double> criteriaCoefficientsUpdate = new HashMap<>();
        // All the coefficients existing
        criteriaCoefficientsUpdate.put(ScoreCriteria.OVERALL_GRADE, 4.0);
        criteriaCoefficientsUpdate.put(ScoreCriteria.RESSOURCE_OVERALL_GRADE, 6.0);
        criteriaCoefficientsUpdate.put(ScoreCriteria.TUTOR_YEAR, 6.0);
        criteriaCoefficientsUpdate.put(ScoreCriteria.STUDENT_IN_DIFFICULTY_MOTIVATION, 1.5);
        criteriaCoefficientsUpdate.put(ScoreCriteria.TUTOR_MOTIVATION, 1.5);

        assertNotEquals(criteriaCoefficientsUpdate, ScoreCalculator.getCriteriaCoefficients());
        // Update all criteria
        assignmentPlatform.setCriteriaCoefficients(criteriaCoefficientsUpdate);
        assertEquals(criteriaCoefficientsUpdate, ScoreCalculator.getCriteriaCoefficients());

        // Reset only OVERALL_GRADE criteria (set default value -> 3.0)
        assignmentPlatform.resetCriteriaCoefficient(ScoreCriteria.OVERALL_GRADE);
        assertNotEquals(criteriaCoefficientsUpdate, ScoreCalculator.getCriteriaCoefficients());

        // Re update the OVERALL_GRADE criteria to 4.0 like the previous update
        assignmentPlatform.setCriteriaCoefficient(ScoreCriteria.OVERALL_GRADE, 4.0);
        assertEquals(criteriaCoefficientsUpdate, ScoreCalculator.getCriteriaCoefficients());

        Map<ScoreCriteria, Double> defaultCriteriaCoefficients = new HashMap<>();
        for (ScoreCriteria sc : ScoreCriteria.values()) {
            defaultCriteriaCoefficients.put(sc, sc.getDefaultCoefficient());
        }
        // Reset all criteria (set default value)
        assignmentPlatform.resetCriteriaCoefficients();
        assertEquals(ScoreCalculator.getCriteriaCoefficients(), defaultCriteriaCoefficients);
    }

    @Test
    public void forcedAssignments() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        assignmentPlatform.addForcedAssignment("POO", st1, sid1);
        // Effective assignments = computed and forced assignments
        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = assignmentPlatform.getEffectiveAssignments("POO");

        // No computed assignment and only one forced assignment
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            assertTrue(assignment.isForced());
            assertTrue(assignment.contains(st1, sid1));
        }
    }

    @Test
    public void forbiddenAssignments() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        // POO : st1 - sid1 COMPUTED -- Test also computeAssignments
        assignmentPlatform.computeAssignments("POO");
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // POO : st1 - sid1 FORBIDDEN (so remove the computed assignment)
        assignmentPlatform.addForbiddenAssignment("POO", st1, sid1);
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = assignmentPlatform.getForbiddenAssignments("POO");

        // Only one forbidden assignment for the "POO" subject (st1 - sid1 FORBIDDEN)
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            assertTrue(assignment.isForbidden());
            assertTrue(assignment.contains(st1, sid1));
        }
    }

    @Test
    public void removeAssignment() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        // All assignments is empty at the beginning so effective and forbidden assignments are empty too
        assertTrue(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getForbiddenAssignments("POO").isEmpty());

        // Compute assignments : EffectiveAssignments contains computed assignments
        assignmentPlatform.computeAssignments("POO");
        assertFalse(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Remove the computed assignment added before (only one is computed st1 - sid1) : EffectiveAssignments becomes empty again
        assignmentPlatform.removeAssignment("POO", st1, sid1);
        assertTrue(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Add forced assignment
        assignmentPlatform.addForcedAssignment("POO", st1, sid1);
        assertFalse(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        assignmentPlatform.removeAssignment("POO", st1, sid1);
        assertTrue(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Add forbidden assignment
        assignmentPlatform.addForbiddenAssignment("POO", st1, sid1);
        assertFalse(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertFalse(assignmentPlatform.getForbiddenAssignments("POO").isEmpty());
        assignmentPlatform.removeAssignment("POO", st1, sid1);
        assertTrue(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getForbiddenAssignments("POO").isEmpty());

        // Add st3 and sid2 in the platform
        assignmentPlatform.addStudentTutor(st3);
        assignmentPlatform.addStudentInDifficulty(sid2);

        // EffectiveAssignments not empty because we just remove the forced assignment st1 - sid1, there is still the computed assignment st3 - sid2
        assignmentPlatform.addForcedAssignment("POO", st1, sid1);
        assignmentPlatform.computeAssignments("POO");
        assignmentPlatform.removeAssignment("POO", st1, sid1);
        assertFalse(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // We also remove the assignment st3 - sid2 so now the effectiveAssignments is empty
        assignmentPlatform.removeAssignment("POO", st3, sid2);
        assertTrue(assignmentPlatform.getAllAssignments("POO").isEmpty());
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
    }

    @Test
    public void resetAssignments() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        assignmentPlatform.addStudentTutor(st3);
        assignmentPlatform.addStudentInDifficulty(sid2);
        // EffectiveAssignments is empty at the beginning
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Compute assignments : EffectiveAssignments contains computed assignments
        assignmentPlatform.computeAssignments("POO");
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Reset computed assignments : EffectiveAssignments becomes empty again
        assignmentPlatform.resetAssignments("POO", AssignmentType.COMPUTED);
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Add forced assignment
        assignmentPlatform.addForcedAssignment("POO", st1, sid1);
        assignmentPlatform.addForcedAssignment("POO", st3, sid2);
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Reset computed and forbidden assignments : effectiveAssignments still contains the forced assignment
        assignmentPlatform.resetAssignments("POO", AssignmentType.COMPUTED);
        assignmentPlatform.resetAssignments("POO", AssignmentType.FORBIDDEN);
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Reset forced assignment : effectiveAssignments becomes empty again
        assignmentPlatform.resetAssignments("POO", AssignmentType.FORCED);
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Add forbidden assignments
        assignmentPlatform.addForbiddenAssignment("POO", st1, sid1);
        assignmentPlatform.addForbiddenAssignment("POO", st3, sid2);
        assertFalse(assignmentPlatform.getForbiddenAssignments("POO").isEmpty());
        assignmentPlatform.resetAssignments("POO", AssignmentType.FORBIDDEN);
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());

        // Add a forced assignment and computed assignments
        assignmentPlatform.addForcedAssignment("POO", st1, sid1);
        assignmentPlatform.computeAssignments("POO");
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Reset only computed assignments : EffectiveAssignments still not empty because effectiveAssignments regroup forced and computed assignments
        assignmentPlatform.resetAssignments("POO", AssignmentType.COMPUTED);
        assertFalse(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
        // Reset forced assignments : EffectiveAssignments becomes empty again because we reset computed (the step before) and the forced (now) assignments
        assignmentPlatform.resetAssignments("POO", AssignmentType.FORCED);
        assertTrue(assignmentPlatform.getEffectiveAssignments("POO").isEmpty());
    }

    @Test
    public void setStudentsApprovalStatusByCriteria() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        CriteriaFilter studentTutorCriteriaFilter1 = new CriteriaFilter(FilterCriteriaType.RESSOURCE_OVERALL_GRADE_SUPERIOR_TO, 13.0, true);
        CriteriaFilter studentTutorCriteriaFilter2 = new CriteriaFilter(FilterCriteriaType.NB_ABSENCES_SUPERIOR_TO, 4, false);

        // Student tutor : Florian Etrillard - Overall grade POO : 17.5 - Nb absences 5

        // POO grade = 17.5 > 13.0 so he is approved
        assignmentPlatform.setStudentsTutorApprovalStatusByCriteria(studentTutorCriteriaFilter1, "POO");
        assertTrue(assignmentPlatform.getStudentTutorByName("Florian", "Etrillard").isApproved("POO"));

        // nb absences = 5 > 4 so he is not approved
        assignmentPlatform.setStudentsTutorApprovalStatusByCriteria(studentTutorCriteriaFilter2, "POO");
        assertFalse(assignmentPlatform.getStudentTutorByName("Florian", "Etrillard").isApproved("POO"));

        CriteriaFilter studentInDifficultyCriteriaFilter1 = new CriteriaFilter(FilterCriteriaType.OVERALL_GRADE_SUPERIOR_TO, 10.0, false);

        // Student in difficulty : Renan Declercq - Overall grade : 11.6

        // All students are approved by default
        assertTrue(assignmentPlatform.getStudentInDifficultyByName("Renan", "Declercq").isApproved("POO"));

        // Overall grade = 11.6 > 10.0 so he is not approved
        assignmentPlatform.setStudentsInDifficultyApprovalStatusByCriteria(studentInDifficultyCriteriaFilter1, "POO");
        assertFalse(assignmentPlatform.getStudentInDifficultyByName("Renan", "Declercq").isApproved("POO"));
    }

    @Test
    public void errorException() throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        HashMap<String, Double> st4Grades = new HashMap<>();
        String st4Enrollment = "GRAPHE";
        st4Grades.put(st4Enrollment, 18.3);
        st4 = new StudentTutor("Jamy", "Powdell", st4Grades, 16.2, MotivationScale.MEDIUM_MOTIVATION, 3, st4Enrollment, 2);

        // Teacher = Samuel Kopa (IHM - POO) can't perform actions on graphe subject
        try {
            assignmentPlatform.addForbiddenAssignment("GRAPHE", st4, sid1);
            fail();
        } catch (NotAllowedException e) {
            assertTrue(true);
        }

        // Philosophie doesn't exist
        try {
            assignmentPlatform.addForcedAssignment("PHILOSOPHIE", st1, sid1);
            fail();
        } catch (RessourceNotFoundException e) {
            assertTrue(true);
        }

        // The student tutor st2 is not registered for POO subject
        try {
            assignmentPlatform.addForbiddenAssignment("POO", st2, sid1);
            fail();
        } catch (StudentNotFoundException e) {
            assertTrue(true);
        }
    }

}
