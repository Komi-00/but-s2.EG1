package gui.controllers;

import assignment.calculation.AssignmentType;
import assignment.tutoring.Student;
import gui.Main;
import gui.controls.CustomAlert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;


public class StudentCardController implements Initializable {
    public static final String IMAGE_PATH = "/gui/resources/images/";
    private static final Image THUMB_DOWN_ACTIVE_IMAGE = new Image(
            StudentCardController.class.getResourceAsStream(IMAGE_PATH + "thumb-down-active.png"));
    private static final Image THUMB_UP_ACTIVE_IMAGE = new Image(
            StudentCardController.class.getResourceAsStream(IMAGE_PATH + "thumb-up-active.png"));
    private static final Image THUMB_DOWN_IMAGE = new Image(StudentCardController.class.getResourceAsStream(IMAGE_PATH + "thumb-down.png"));
    private static final Image THUMB_UP_IMAGE = new Image(StudentCardController.class.getResourceAsStream(IMAGE_PATH + "thumb-up.png"));
    private static final Image CROSS_IMAGE = new Image(StudentCardController.class.getResourceAsStream(IMAGE_PATH + "cancel.png"));
    private static final Image LINK_IMAGE = new Image(StudentCardController.class.getResourceAsStream(IMAGE_PATH + "lock.png"));

    @FXML
    private ToggleButton rejectToggle;
    @FXML
    private ToggleButton approveToggle;
    @FXML
    private ToggleGroup approvalToggles;
    @FXML
    private Label nameLabel;
    @FXML
    private Tooltip studentNameTooltip;
    @FXML
    private Label overallGradeLabel;
    @FXML
    private Label absencesLabel;
    @FXML
    private Label ressourceOverallGradeLabel;
    @FXML
    private Button editButton;
    @FXML
    private ImageView statusImage;
    @FXML
    private ImageView approveImage;
    @FXML
    private ImageView rejectImage;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private Button forbiddenAssignmentButton;
    @FXML
    private Button excludeButton;
    @FXML
    private AnchorPane root;
    private Student student;

    public StudentCardController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/resources/fxml/StudentCard.fxml"));
            loader.setController(this);
            this.root = loader.load();
        } catch (IOException e) {
            new CustomAlert(Alert.AlertType.ERROR, "Erreur chargement composant graphique" + e).show();
        }
    }

    public static void toggleVisibility(Node element, boolean visible) {
        element.setVisible(visible);
        element.setManaged(visible);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.approvalToggles.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            this.student.setApprovalStatus(Main.RESSOURCE_NAME, Objects.equals(newToggle, approveToggle));
            this.updateApprovalToggles();
        });
        this.showApprovalToggles(true);
        this.showExtraButtons(false);

    }

    public void showApprovalToggles(boolean show) {
        toggleVisibility(this.rejectToggle, show);
        toggleVisibility(this.approveToggle, show);
    }

    public void showExtraButtons(boolean show) {
        toggleVisibility(this.forbiddenAssignmentButton, show);
        toggleVisibility(this.excludeButton, show);
    }

    public void addExcludeButtonHandler(EventHandler<ActionEvent> eventHandler) {
        this.excludeButton.setOnAction(eventHandler);
        toggleVisibility(this.excludeButton, true);
    }

    public void addForbiddenButton(EventHandler<ActionEvent> eventHandler) {
        this.forbiddenAssignmentButton.setOnAction(eventHandler);
        toggleVisibility(this.forbiddenAssignmentButton, true);
    }

    public void setStudent(Student student) {
        this.student = student;
        this.updateView();
    }

    public void removeHighlight() {
        this.setStatus(null);
    }

    public void highlight(AssignmentType type) {
        this.setStatus(type);
    }

    public void updateView() {
        String studentName = this.student.getFirstName() + " " + this.student.getLastName();
        this.nameLabel.setText(studentName);
        this.studentNameTooltip.setText(studentName);
        this.overallGradeLabel.setText("" + decimalFormat().format(this.student.getOverallGrade()));
        this.ressourceOverallGradeLabel.setText("" + decimalFormat().format(this.student.getRessourceGrade(Main.RESSOURCE_NAME)));
        this.absencesLabel.setText("" + this.student.getNbAbsences());
        this.updateApprovalToggles();
    }

    private void updateApprovalToggles() {
        boolean isApproved = student.isApproved(Main.RESSOURCE_NAME);
        if (isApproved) {
            this.approveImage.setImage(THUMB_UP_ACTIVE_IMAGE);
            this.rejectImage.setImage(THUMB_DOWN_IMAGE);
        } else {
            this.approveImage.setImage(THUMB_UP_IMAGE);
            this.rejectImage.setImage(THUMB_DOWN_ACTIVE_IMAGE);
        }
    }

    private void setStatus(AssignmentType type) {
        boolean isStatusInvisible = (type == null || type == AssignmentType.COMPUTED);
        this.statusImage.setVisible(!isStatusInvisible);
        if (type == AssignmentType.FORCED) {
            this.statusImage.setImage(LINK_IMAGE);
        }
        if (type == AssignmentType.FORBIDDEN) {
            this.statusImage.setImage(CROSS_IMAGE);
        }
    }

    public AnchorPane getRoot() {
        return root;
    }

    @FXML
    public void openStudentInfoButtonAction() {
        StudentInfoController studentInfoController = new StudentInfoController(this.student);

    }

    private DecimalFormat decimalFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        df.setMinimumIntegerDigits(2);
        df.setMaximumIntegerDigits(2);
        return df;
    }

}