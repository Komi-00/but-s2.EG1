package gui.controls;

import assignment.calculation.AssignmentType;
import assignment.tutoring.Student;
import gui.Main;
import gui.controllers.StudentCardController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class StudentCardCell<T1 extends Student, T2 extends Student> extends ListCell<T1> {
    private final StudentCardController studentCardController;
    private final Node root;
    private final ListView<T2> oppositeListView;

    public StudentCardCell(ListView<T2> oppositeListView) {
        this.studentCardController = new StudentCardController();
        this.root = studentCardController.getRoot();
        this.oppositeListView = oppositeListView;
    }

    private ListView<T2> getOppositeListView() {
        return oppositeListView;
    }

    @Override
    protected void updateItem(T1 cellStudent, boolean empty) {
        super.updateItem(cellStudent, empty);
        if (empty) {
            this.setGraphic(null);
        } else {
            this.studentCardController.setStudent(cellStudent);
            this.studentCardController.removeHighlight();

            boolean listviewIsFocused = this.getListView().isFocused();
            boolean oppositeListviewIsFocused = oppositeListView.isFocused();

            T1 listviewSelectedStudent = this.getListView().getSelectionModel().getSelectedItem();
            T2 oppositeListviewSelectedStudent = this.getOppositeListView().getSelectionModel().getSelectedItem();

            if ((!listviewIsFocused && oppositeListviewIsFocused) || (cellStudent.equals(listviewSelectedStudent))) {
                AssignmentType assignmentType = Main.getAssignmentType(cellStudent, oppositeListviewSelectedStudent);
                this.studentCardController.highlight(assignmentType);
            }

            this.setGraphic(root);
        }
    }
}