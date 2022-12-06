package assignment.tutoring;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import assignment.calculation.ScoreCriteria;

public class StudentInDifficulty extends Student {
    private final Set<String> enrollments;

    public StudentInDifficulty(String firstName, String lastName, Map<String, Double> grades,
                               double overallGrade, MotivationScale motivationScale, int nbAbsences,
                               Set<String> enrollments) {
        super(firstName, lastName, grades, overallGrade, motivationScale, nbAbsences);
        this.enrollments = enrollments;
    }

    public StudentInDifficulty(String firstName, String lastName, Map<String, Double> grades,
                               double overallGrade, MotivationScale motivationScale) {
        this(firstName, lastName, grades, overallGrade, motivationScale, 0, new HashSet<>());
    }

    public StudentInDifficulty(String firstName, String lastName, double overallGrade,
                               MotivationScale motivationScale) {
        this(firstName, lastName, new HashMap<>(), overallGrade, motivationScale);
    }

    public StudentInDifficulty(String firstName, String lastName, double overallGrade) {
        this(firstName, lastName, new HashMap<>(), overallGrade, MotivationScale.LOW_MOTIVATION);
    }

    @Override
    public boolean isStudentInDifficulty() {
        return true;
    }

    @Override
    public boolean isStudentTutor() {
        return false;
    }

    @Override
    public Map<ScoreCriteria, Double> getCriteriaValues(String ressourceName) {
        Map<ScoreCriteria, Double> criteriaValues = new EnumMap<>(ScoreCriteria.class);
        criteriaValues.put(ScoreCriteria.OVERALL_GRADE, this.overallGrade);
        criteriaValues.put(ScoreCriteria.STUDENT_IN_DIFFICULTY_MOTIVATION,
                (double) this.motivationScale.ordinal());
        criteriaValues.put(ScoreCriteria.RESSOURCE_OVERALL_GRADE, this.grades.get(ressourceName));
        return criteriaValues;
    }

    public Set<String> getEnrollments() {
        return this.enrollments;
    }

    @Override
    public String toString() {
        return "SID " + super.toString();
    }
}
