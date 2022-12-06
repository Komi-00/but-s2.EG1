package gui.controllers;

import assignment.calculation.Assignment;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentTutor;
import gui.Main;
import gui.controls.AssignmentCardCell;
import gui.controls.CustomDangerConfirmAlert;
import gui.helpers.AssignmentSortingOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;


public class AssignmentsViewController implements Initializable {
    private final ObservableList<List<Assignment<StudentTutor, StudentInDifficulty>>> assignmentsObservableList;
    private final FilteredList<List<Assignment<StudentTutor, StudentInDifficulty>>> assignmentsFilteredList;

    private final ToggleGroup sortToggleGroup;
    @FXML
    private ToggleButton effectiveAssignmentsToggleButton;
    @FXML
    private ToggleButton forbiddenAssignmentsToggleButton;
    @FXML
    private ToggleGroup assignmentsTypeToggleGroup;
    @FXML
    private TextField searchAssignmentTextField;
    @FXML
    private SplitMenuButton sortSplitMenuButton;
    @FXML
    private ListView<List<Assignment<StudentTutor, StudentInDifficulty>>> assignmentsList;
    @FXML
    private Button clearButton;


    public AssignmentsViewController() {
        this.assignmentsObservableList = FXCollections.observableArrayList();
        this.assignmentsFilteredList = new FilteredList<>(this.assignmentsObservableList);
        this.sortToggleGroup = new ToggleGroup();
    }

    public static List<RadioMenuItem> setupSortMenuItems(ToggleGroup toggleGroup) {
        List<RadioMenuItem> list = new ArrayList<>();
        for (AssignmentSortingOption option : AssignmentSortingOption.values()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(option.getLabel());
            radioMenuItem.setToggleGroup(toggleGroup);
            radioMenuItem.setUserData(option);
            list.add(radioMenuItem);
        }
        return list;
    }

    public static Collection<List<Assignment<StudentTutor, StudentInDifficulty>>> groupAssignmentsByLeft(List<? extends Assignment<StudentTutor, StudentInDifficulty>> assignments) {
        Map<StudentTutor, List<Assignment<StudentTutor, StudentInDifficulty>>> groupedAssignments = new HashMap<>();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            if (!groupedAssignments.containsKey(assignment.getLeft())) {
                List<Assignment<StudentTutor, StudentInDifficulty>> list = new ArrayList<>();
                list.add(assignment);
                groupedAssignments.put(assignment.getLeft(), list);
            } else {
                groupedAssignments.get(assignment.getLeft()).add(assignment);
            }
        }
        return groupedAssignments.values();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.assignmentsList.setCellFactory(e -> new AssignmentCardCell());
        this.assignmentsList.setItems(this.assignmentsFilteredList);

        this.sortSplitMenuButton.getItems().addAll(setupSortMenuItems(this.sortToggleGroup));
        this.sortToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                AssignmentSortingOption studentSortingOption = (AssignmentSortingOption) newValue.getUserData();
                this.assignmentsObservableList.sort(studentSortingOption.getComparator());
            }
        });

        this.searchAssignmentTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            this.assignmentsFilteredList.setPredicate(assignments -> {
                for (Assignment<StudentTutor, StudentInDifficulty> a : assignments) {
                    boolean tutorNameMatches = a.getLeft().getFullName().toLowerCase().contains(newValue.toLowerCase());
                    boolean inDifficultyNameMatches = a.getRight().getFullName().toLowerCase().contains(newValue.toLowerCase());
                    if (tutorNameMatches || inDifficultyNameMatches) return true;
                }
                return false;
            });
        });


        this.assignmentsTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                this.updateAssignments();
            }
        });
    }


    @FXML
    public void handleLaunchButtonAction() {
        Main.computeAssignments();
        this.updateAssignments();
    }

    private void updateAssignments() {
        Collection<List<Assignment<StudentTutor, StudentInDifficulty>>> tutorGroupedAssignments;
        if (this.forbiddenAssignmentsToggleButton.isSelected()) {
            tutorGroupedAssignments = groupAssignmentsByLeft(Main.getForbiddenAssignments());
        } else {
            tutorGroupedAssignments = groupAssignmentsByLeft(Main.getEffectiveAssignments());
        }
        this.assignmentsObservableList.setAll(tutorGroupedAssignments);
    }

    @FXML
    public void handleResetButtonAction() {
        boolean confirm = new CustomDangerConfirmAlert(Alert.AlertType.CONFIRMATION, "Etes vous sur de vouloir supprimer vos affectations calculées ?").showWithResult();
        if (confirm) {
            Main.resetAssignments();
            this.updateAssignments();
        }
    }

    @FXML
    public void handleSortButtonAction() {
        Collections.reverse(this.assignmentsObservableList);
    }

    @FXML
    public void handleClearSearchAssignmentFieldButtonAction() {
        this.searchAssignmentTextField.setText("");
    }


}
