package gui.controllers;

import assignment.calculation.Assignment;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;
import gui.Main;
import gui.controls.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentCardController {
    private final List<StudentCardController> studentsInDifficultyCardControllers;
    private final List<Assignment<StudentTutor, StudentInDifficulty>> assignments;
    private StudentCardController studentTutorCardController;
    @FXML
    private VBox studentsTutorVBox;
    @FXML
    private VBox studentsInDifficultyVBox;
    @FXML
    private AnchorPane root;

    public AssignmentCardController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/resources/fxml/AssignmentCard.fxml"));
            loader.setController(this);
            this.root = loader.load();
        } catch (IOException e) {
            new CustomAlert(Alert.AlertType.ERROR, "Erreur chargement composant graphique" + e).show();
        }

        this.assignments = new ArrayList<>();
        this.studentsInDifficultyCardControllers = new ArrayList<>();
    }

    private Node getStudentTutorRoot() {
        return this.studentTutorCardController.getRoot();
    }

    private List<Node> getStudentInDifficultyRoots() {
        List<Node> studentInDifficultyRoots = new ArrayList<>();
        for (StudentCardController studentInDifficultyCardController : this.studentsInDifficultyCardControllers) {
            studentInDifficultyRoots.add(studentInDifficultyCardController.getRoot());
        }
        return studentInDifficultyRoots;
    }

    public AnchorPane getRoot() {
        return this.root;
    }

    private void updateStudentsInDifficulty() {
        this.studentsInDifficultyCardControllers.clear();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : this.assignments) {
            StudentCardController studentInDifficultyCardController = newStudentInDifficultyController(assignment);
            this.studentsInDifficultyCardControllers.add(studentInDifficultyCardController);
        }
    }


    private StudentCardController newStudentInDifficultyController(Assignment<StudentTutor, StudentInDifficulty> assignment) {
        StudentCardController studentCardController = new StudentCardController();
        StudentTutor studentTutor = assignment.getLeft();
        StudentInDifficulty studentInDifficulty = assignment.getRight();

        studentCardController.highlight(assignment.getType());
        studentCardController.showApprovalToggles(false);
        studentCardController.addForbiddenButton(e -> {
            Main.addManualAssignment(studentTutor, studentInDifficulty, true);
            new CustomAlert(Alert.AlertType.INFORMATION, "Interdiction d'affectation prise en compte, veuillez relancer l'affectation pour appliquer les changements").show();
        });
        studentCardController.addExcludeButtonHandler(e -> {
            Main.excludeStudent(studentInDifficulty);
            new CustomAlert(Alert.AlertType.INFORMATION, "Désistement de l'étudiant pris en compte, veuillez relancer l'affectation pour appliquer les changements").show();
        });
        studentCardController.setStudent(assignment.getRight());

        return studentCardController;
    }

    private StudentCardController newStudentTutorCardController(StudentTutor studentTutor) {
        StudentCardController studentCardController = new StudentCardController();

        studentCardController.showApprovalToggles(false);
        studentCardController.addExcludeButtonHandler(e -> {
            Main.excludeStudent(studentTutor);
            new CustomAlert(Alert.AlertType.INFORMATION, "Désistement de l'étudiant pris en compte, veuillez relancer l'affectation pour appliquer les changements").show();
        });
        studentCardController.setStudent(studentTutor);

        return studentCardController;
    }

    private void updateStudentTutor() {
        if (this.assignments.size() > 0) {
            this.studentTutorCardController = newStudentTutorCardController(this.assignments.get(0).getLeft());
        }
    }

    private double getScoreMinimum() {
        double scoreMinimum = Double.MAX_VALUE;
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : this.assignments) {
            scoreMinimum = Math.min(scoreMinimum, assignment.getScore());
        }
        return scoreMinimum;
    }

    public void setColorAccordingScore(double score) {
        // TODO: change that to be based on min and max score
        this.root.setStyle("-fx-border-radius: 3;");
        if (score < 3) {
            this.root.setStyle("-fx-border-color: #25b425;");
        } else if (score < 5) {
            this.root.setStyle("-fx-border-color: #e59604;");
        } else {
            this.root.setStyle("-fx-border-color: #d31515;");
        }
    }


    public void setAssignments(List<Assignment<StudentTutor, StudentInDifficulty>> assignments) {
        this.assignments.clear();
        this.assignments.addAll(assignments);
        this.updateStudentsInDifficulty();
        this.updateStudentTutor();
        this.updateView();
    }

    private void updateView() {
        this.studentsTutorVBox.getChildren().clear();
        this.studentsInDifficultyVBox.getChildren().clear();
        this.studentsTutorVBox.getChildren().add(this.getStudentTutorRoot());
        this.studentsInDifficultyVBox.getChildren().addAll(this.getStudentInDifficultyRoots());
        this.setColorAccordingScore(this.getScoreMinimum());
    }
}
