package gui.controllers;

import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;
import gui.Main;
import gui.controls.StudentCardCell;
import gui.helpers.StudentSortingOption;
import gui.helpers.StudentsSelectionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class StudentsViewController implements Initializable {
    private final ObservableList<StudentTutor> studentsTutorObservableList;
    private final FilteredList<StudentTutor> studentsTutorFilteredList;
    private final ObservableList<StudentInDifficulty> studentsInDifficultyObservableList;
    private final FilteredList<StudentInDifficulty> studentsInDifficultyFilteredList;
    private final ToggleGroup leftSortToggleGroup;
    private final ToggleGroup rightSortToggleGroup;
    @FXML
    private ListView<StudentInDifficulty> studentsInDifficultyList;
    @FXML
    private ListView<StudentTutor> studentsTutorList;
    @FXML
    private Button addForbiddenAssignmentButton;
    @FXML
    private Button addForcedAssignmentButton;
    @FXML
    private Button removeAssignmentButton;
    @FXML
    private TextField searchTutorTextField;
    @FXML
    private TextField searchInDifficultyTextField;
    @FXML
    private Button importStudentsButton;
    @FXML
    private Button importFiltersButton;
    @FXML
    private SplitMenuButton leftSortSplitMenuButton;
    @FXML
    private SplitMenuButton rightSortSplitMenuButton;
    private StudentsSelectionHandler studentsSelectionHandler;

    public StudentsViewController() {
        this.leftSortToggleGroup = new ToggleGroup();
        this.rightSortToggleGroup = new ToggleGroup();
        this.studentsTutorObservableList = FXCollections.observableArrayList();
        this.studentsTutorFilteredList = new FilteredList<>(this.studentsTutorObservableList);
        this.studentsInDifficultyObservableList = FXCollections.observableArrayList();
        this.studentsInDifficultyFilteredList = new FilteredList<>(this.studentsInDifficultyObservableList);
    }

    public static List<RadioMenuItem> setupSortMenuItems(ToggleGroup toggleGroup) {
        List<RadioMenuItem> list = new ArrayList<>();
        for (StudentSortingOption option : StudentSortingOption.values()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(option.getLabel());
            radioMenuItem.setToggleGroup(toggleGroup);
            radioMenuItem.setUserData(option);
            list.add(radioMenuItem);
        }
        return list;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.studentsTutorList.setItems(this.studentsTutorFilteredList);
        this.studentsInDifficultyList.setItems(this.studentsInDifficultyFilteredList);

        // TODO: Move "duplicated" list handling code into custom component
        this.studentsSelectionHandler = new StudentsSelectionHandler(addForcedAssignmentButton, addForbiddenAssignmentButton, removeAssignmentButton, studentsTutorList, studentsInDifficultyList);
        this.studentsTutorList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super StudentTutor>) change -> this.studentsSelectionHandler.applySelectionResults());
        this.studentsInDifficultyList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super StudentInDifficulty>) change -> this.studentsSelectionHandler.applySelectionResults());
        this.studentsTutorList.setOnMouseClicked(change -> this.studentsSelectionHandler.applySelectionResults());
        this.studentsInDifficultyList.setOnMouseClicked(change -> this.studentsSelectionHandler.applySelectionResults());

        this.studentsInDifficultyList.setCellFactory(e -> new StudentCardCell<>(this.studentsTutorList));
        this.studentsTutorList.setCellFactory(e -> new StudentCardCell<>(this.studentsInDifficultyList));

        this.searchTutorTextField.textProperty().addListener((obs, oldValue, newValue) -> this.studentsTutorFilteredList.setPredicate(st -> st.getFullName().toLowerCase().contains(newValue.toLowerCase())));
        this.searchInDifficultyTextField.textProperty().addListener((obs, oldValue, newValue) -> this.studentsInDifficultyFilteredList.setPredicate(sid -> sid.getFullName().toLowerCase().contains(newValue.toLowerCase())));

        this.leftSortSplitMenuButton.getItems().addAll(setupSortMenuItems(this.leftSortToggleGroup));
        this.rightSortSplitMenuButton.getItems().addAll(setupSortMenuItems(this.rightSortToggleGroup));

        this.leftSortToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                StudentSortingOption studentSortingOption = (StudentSortingOption) newValue.getUserData();
                this.studentsTutorObservableList.sort(studentSortingOption.getComparator());
            }
        });

        this.rightSortToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                StudentSortingOption studentSortingOption = (StudentSortingOption) newValue.getUserData();
                this.studentsInDifficultyObservableList.sort(studentSortingOption.getComparator());
            }
        });
    }

    @FXML
    public void handleImportFiltersButtonAction() {
        File selectedFile = openFileChooser(this.importFiltersButton);
        if (selectedFile == null)
            return;

        Main.importFilters(selectedFile.getAbsolutePath());
        this.studentsTutorList.refresh();
        this.studentsInDifficultyList.refresh();
    }

    @FXML
    public void handleAddForbiddenAssignmentButtonAction() {
        this.addAssignment(true);
    }

    @FXML
    public void handleAddForcedAssignmentButtonAction() {
        this.addAssignment(false);
    }

    @FXML
    public void handleRemoveAssignmentButtonAction() {
        StudentTutor studentTutor = this.studentsTutorList.getSelectionModel().getSelectedItem();
        StudentInDifficulty studentInDifficulty = this.studentsInDifficultyList.getSelectionModel().getSelectedItem();
        Main.removeAssignment(studentTutor, studentInDifficulty);
        this.studentsSelectionHandler.applySelectionResults();
    }

    private void addAssignment(boolean isForbidden) {
        StudentTutor studentTutor = this.studentsTutorList.getSelectionModel().getSelectedItem();
        StudentInDifficulty studentInDifficulty = this.studentsInDifficultyList.getSelectionModel().getSelectedItem();
        Main.addManualAssignment(studentTutor, studentInDifficulty, isForbidden);
        this.studentsSelectionHandler.applySelectionResults();
    }

    private File openFileChooser(Node node) {
        FileChooser fileChooser = new FileChooser();
        File startDir = new File(Main.RES_PATH);
        fileChooser.setInitialDirectory(startDir.exists() ? startDir : null);
        fileChooser.setTitle("Select File");

        return fileChooser.showOpenDialog(node.getScene().getWindow());
    }

    @FXML
    public void handleImportStudentsButtonAction() {
        File selectedFile = openFileChooser(this.importStudentsButton);
        if (selectedFile == null)
            return;

        Main.importStudents(selectedFile.getAbsolutePath());
        this.studentsTutorObservableList.setAll(Main.getStudentsTutors());
        this.studentsInDifficultyObservableList.setAll(Main.getStudentsInDifficulty());
    }

    @FXML
    public void handleStudentTutorSortButtonAction() {
        Collections.reverse(this.studentsTutorObservableList);
    }

    @FXML
    public void handleStudentInDifficultySortButtonAction() {
        Collections.reverse(this.studentsInDifficultyObservableList);
    }

    @FXML
    public void handleClearSearchTutorFieldButtonAction() {
        this.searchTutorTextField.setText("");
    }

    @FXML
    public void handleClearSearchInDifficultyFieldButtonAction() {
        this.searchInDifficultyTextField.setText("");
    }

}
