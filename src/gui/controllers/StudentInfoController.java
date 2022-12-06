package gui.controllers;

import assignment.tutoring.Student;
import assignment.tutoring.StudentTutor;
import gui.Main;
import gui.controls.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentInfoController implements Initializable {
    private final Student student;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label overallGradeLabel;
    @FXML
    private Label ressourceOverallGradeLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label motivationLabel;
    @FXML
    private Label nbAbsencesLabel;
    @FXML
    private Button closeButton;
    @FXML
    private VBox root;

    public StudentInfoController(Student student) {
        this.student = student;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/resources/fxml/StudentInfo.fxml"));
        loader.setController(this);
        try {
            this.root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Information de l'étudiant");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            new CustomAlert(Alert.AlertType.ERROR, "Erreur chargement composant graphique" + e).show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lastNameLabel.setText(this.student.getLastName());
        this.firstNameLabel.setText(this.student.getFirstName());
        this.overallGradeLabel.setText("" + this.student.getOverallGrade());
        this.ressourceOverallGradeLabel.setText("" + this.student.getRessourceGrade(Main.RESSOURCE_NAME));
        this.nbAbsencesLabel.setText("" + this.student.getNbAbsences());
        if (this.student.isStudentTutor()) {
            StudentTutor studentTutor = (StudentTutor) student;
            this.yearLabel.setText("" + studentTutor.getSchoolYear());
        } else {
            this.yearLabel.setText("1");
        }
        this.motivationLabel.setText(this.student.getMotivationScale().toString());
    }

    @FXML
    public void handleCloseWindowButtonAction() {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }

    public VBox getView() {
        return this.root;
    }
}
