package gui.helpers;

import assignment.tutoring.Student;

import java.util.Comparator;

import static gui.Main.RESSOURCE_NAME;

public enum StudentSortingOption {
    FIRST_NAME("Prénom", Comparator.comparing((Student s) -> s.getFirstName())),
    LAST_NAME("Nom", Comparator.comparing((Student s) -> s.getLastName())),
    OVERALL_GRADE("Moyenne générale", Comparator.comparingDouble((Student s) -> s.getOverallGrade())),
    RESSOURCE_OVERALL_GRADE("Moyenne ressource", Comparator.comparingDouble((Student s) -> s.getRessourceGrade(RESSOURCE_NAME))),
    NB_ABSENCES("Nb absences", Comparator.comparingInt((Student s) -> s.getNbAbsences()));

    private final String label;
    private final Comparator<Student> comparator;

    StudentSortingOption(String label, Comparator<Student> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public String getLabel() {
        return this.label;
    }

    public Comparator<Student> getComparator() {
        return this.comparator;
    }
}