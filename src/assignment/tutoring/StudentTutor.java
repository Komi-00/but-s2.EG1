package assignment.tutoring;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import assignment.calculation.ScoreCriteria;

public class StudentTutor extends Student {
    private final int schoolYear;
    private final String enrollment;

    public StudentTutor(String firstName, String lastName, Map<String, Double> grades, double overallGrade, MotivationScale motivationScale, int nbAbsences,
            String enrollment, int schoolYear) {
        super(firstName, lastName, grades, overallGrade, motivationScale, nbAbsences);
        this.enrollment = enrollment;
        this.schoolYear = schoolYear;
    }

    public StudentTutor(String firstName, String lastName, Map<String, Double> grades, double overallGrade, MotivationScale motivationScale, int schoolYear) {
        this(firstName, lastName, grades, overallGrade, motivationScale, 0, null, schoolYear);
    }

    public StudentTutor(String firstName, String lastName, double overallGrade, MotivationScale motivationScale, int schoolYear) {
        this(firstName, lastName, new HashMap<>(), overallGrade, motivationScale, schoolYear);
    }

    public StudentTutor(String firstName, String lastName, double overallGrade, int schoolYear) {
        this(firstName, lastName, overallGrade, MotivationScale.LOW_MOTIVATION, schoolYear);
    }

    @Override
    public boolean isStudentInDifficulty() {
        return false;
    }

    @Override
    public boolean isStudentTutor() {
        return true;
    }

    @Override
    public boolean canHaveMultiple() {
        return this.schoolYear == 3;
    }

    @Override
    public Map<ScoreCriteria, Double> getCriteriaValues(String ressourceName) {
        Map<ScoreCriteria, Double> criteriaValues = new EnumMap<>(ScoreCriteria.class);
        criteriaValues.put(ScoreCriteria.OVERALL_GRADE, this.overallGrade);
        criteriaValues.put(ScoreCriteria.TUTOR_MOTIVATION, (double) this.motivationScale.ordinal());
        criteriaValues.put(ScoreCriteria.TUTOR_YEAR, (double) this.schoolYear);
        criteriaValues.put(ScoreCriteria.RESSOURCE_OVERALL_GRADE, this.grades.get(ressourceName));
        return criteriaValues;
    }

    public String getEnrollment() {
        return this.enrollment;
    }

    @Override
    public String toString() {
        return "ST " + super.toString();
    }

    public int getSchoolYear() {
        return schoolYear;
    }

}
