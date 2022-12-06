package gui.helpers;

import assignment.calculation.Assignment;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;
import gui.Main;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


public class StudentsSelectionHandler {
    private final Button addForcedAssignmentButton;
    private final Button removeAssignmentButton;
    private final Button addForbiddenAssignmentButton;
    private final ListView<StudentTutor> studentsTutorList;
    private final ListView<StudentInDifficulty> studentsInDifficultyList;

    public StudentsSelectionHandler(Button addForcedAssignmentButton,
                                    Button addForbiddenAssignmentButton,
                                    Button removeAssignmentButton,
                                    ListView<StudentTutor> studentsTutorList, ListView<StudentInDifficulty> studentsInDifficultyList) {
        this.addForcedAssignmentButton = addForcedAssignmentButton;
        this.addForbiddenAssignmentButton = addForbiddenAssignmentButton;
        this.removeAssignmentButton = removeAssignmentButton;
        this.studentsTutorList = studentsTutorList;
        this.studentsInDifficultyList = studentsInDifficultyList;
    }

    public void applySelectionResults() {
        this.studentsTutorList.refresh();
        this.studentsInDifficultyList.refresh();
        this.disableAllButtons(true);
        if (!this.hasTwoElementsSelected()) return;

        Assignment<StudentTutor, StudentInDifficulty> assignment = Main.getAssignment(this.getSelectedStudentTutor(), this.getSelectedStudentInDifficulty());
        this.changeButtonStatusByAssignmentType(assignment);

    }

    public void disableAllButtons(boolean status) {
        this.removeAssignmentButton.setDisable(status);
        this.addForcedAssignmentButton.setDisable(status);
        this.addForbiddenAssignmentButton.setDisable(status);
    }

    public void changeButtonStatusByAssignmentType(Assignment<StudentTutor, StudentInDifficulty> assignment) {
        if (assignment == null || assignment.isComputed()) {
            this.addForbiddenAssignmentButton.setDisable(false);
            this.addForcedAssignmentButton.setDisable(false);
            return;
        }
        this.removeAssignmentButton.setDisable(false);
        if (assignment.isForbidden()) this.addForcedAssignmentButton.setDisable(false);
        if (assignment.isForced()) this.addForbiddenAssignmentButton.setDisable(false);
    }

    private StudentTutor getSelectedStudentTutor() {
        return this.studentsTutorList.getSelectionModel().getSelectedItem();
    }

    private StudentInDifficulty getSelectedStudentInDifficulty() {
        return this.studentsInDifficultyList.getSelectionModel().getSelectedItem();
    }

    private boolean hasTwoElementsSelected() {
        return (this.getSelectedStudentTutor() != null && this.getSelectedStudentInDifficulty() != null);
    }
}