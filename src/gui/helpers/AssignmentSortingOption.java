package gui.helpers;

import assignment.calculation.Assignment;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;

import java.util.Comparator;
import java.util.List;

import static gui.Main.RESSOURCE_NAME;

public enum AssignmentSortingOption {
    OVERALL_GRADE_DIFFERENCE("Différence moyenne générale", Comparator.comparingDouble(assignments -> {
        double overallGrade = 0;
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            overallGrade += assignment.getLeft().getOverallGrade() - assignment.getRight().getOverallGrade();
        }
        return overallGrade / assignments.size();
    })),
    RESSOURCE_OVERALL_GRADE_DIFFERENCE("Différence moyenne ressource", Comparator.comparingDouble(assignments -> {
        double ressourceOverallGrade = 0;
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            ressourceOverallGrade += assignment.getLeft().getRessourceGrade(RESSOURCE_NAME) - assignment.getRight().getRessourceGrade(RESSOURCE_NAME);
        }
        return ressourceOverallGrade / assignments.size();
    })),
    SCORE("Score", Comparator.comparingDouble(assignments -> {
        double score = 0;
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            score += assignment.getScore();
        }
        return score / assignments.size();
    }));

    private final String label;
    private final Comparator<List<Assignment<StudentTutor, StudentInDifficulty>>> comparator;

    AssignmentSortingOption(String label, Comparator<List<Assignment<StudentTutor, StudentInDifficulty>>> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public String getLabel() {
        return this.label;
    }

    public Comparator<List<Assignment<StudentTutor, StudentInDifficulty>>> getComparator() {
        return this.comparator;
    }
}