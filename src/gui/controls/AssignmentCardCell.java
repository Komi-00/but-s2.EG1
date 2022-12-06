package gui.controls;

import assignment.calculation.Assignment;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;
import gui.controllers.AssignmentCardController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.util.List;

public class AssignmentCardCell extends ListCell<List<Assignment<StudentTutor, StudentInDifficulty>>> {
    private final AssignmentCardController assignmentCardController;
    private final Node assignmentCardRoot;

    public AssignmentCardCell() {
        this.assignmentCardController = new AssignmentCardController();
        this.assignmentCardRoot = assignmentCardController.getRoot();
    }

    @Override
    protected void updateItem(List<Assignment<StudentTutor, StudentInDifficulty>> cellAssignments, boolean empty) {
        super.updateItem(cellAssignments, empty);
        if (empty) {
            this.setGraphic(null);
        } else {
            this.assignmentCardController.setAssignments(cellAssignments);
            this.setGraphic(this.assignmentCardRoot);
        }
    }
}
