package assignment.tutoring;

import java.util.HashMap;
import java.util.Map;

import assignment.calculation.IAssignable;

public abstract class Student extends Person implements IAssignable, ICriteriaFilterable {
    protected Map<String, Double> grades;
    protected double overallGrade;
    protected MotivationScale motivationScale;
    protected int nbAbsences;
    protected Map<String, Boolean> approvalStatus;

    protected Student(String firstName, String lastName, Map<String, Double> grades, double overallGrade, MotivationScale motivationScale, int nbAbsences) {
        super(firstName, lastName);
        this.grades = grades;
        this.overallGrade = overallGrade;
        this.motivationScale = motivationScale;
        this.nbAbsences = nbAbsences;
        this.approvalStatus = new HashMap<>();
    }

    protected Student(String firstName, String lastName, Map<String, Double> grades, double overallGrade, MotivationScale motivationScale) {
        this(firstName, lastName, grades, overallGrade, motivationScale, 0);
    }

    protected Student(String firstName, String lastName) {
        this(firstName, lastName, new HashMap<>(), 0, MotivationScale.LOW_MOTIVATION, 0);
    }

    public abstract boolean isStudentInDifficulty();

    public abstract boolean isStudentTutor();

    @Override
    public boolean isCriteriaFilterSatisfied(CriteriaFilter criteriaFilter, String ressourceName) {
        double threshold = criteriaFilter.getThreshold();
        // TODO: improve this
        return switch (criteriaFilter.getFilterCriteriaType()) {
        case OVERALL_GRADE_INFERIOR_TO -> this.overallGrade < threshold;
        case OVERALL_GRADE_SUPERIOR_TO -> this.overallGrade > threshold;
        case NB_ABSENCES_INFERIOR_TO -> this.nbAbsences < threshold;
        case NB_ABSENCES_SUPERIOR_TO -> this.nbAbsences > threshold;
        case RESSOURCE_OVERALL_GRADE_INFERIOR_TO -> this.grades.getOrDefault(ressourceName, Double.POSITIVE_INFINITY) < threshold;
        case RESSOURCE_OVERALL_GRADE_SUPERIOR_TO -> this.grades.getOrDefault(ressourceName, Double.NEGATIVE_INFINITY) > threshold;
        };
    }

    public Boolean setApprovalStatus(String ressourceName, boolean value) {
        return this.approvalStatus.put(ressourceName, value);
    }

    @Override
    public boolean isFictive() {
        return false;
    }

    @Override
    public boolean canHaveMultiple() {
        return false;
    }

    public double getRessourceGrade(String ressourceName) {
        return this.grades.get(ressourceName);
    }

    public double getOverallGrade() {
        return this.overallGrade;
    }

    @Override
    public boolean isApproved(String ressourceName) {
        return this.approvalStatus.getOrDefault(ressourceName, true);
    }

    public int getNbAbsences() {
        return this.nbAbsences;
    }

    public MotivationScale getMotivationScale() {
        return motivationScale;
    }

}
