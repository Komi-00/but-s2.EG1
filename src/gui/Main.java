package gui;

import assignment.AssignmentPlatform;
import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;
import assignment.calculation.ScoreCriteria;
import assignment.input.IncorrectFileFormatException;
import assignment.tutoring.*;
import gui.controls.CustomAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main extends Application {
    public static final String RES_PATH = System.getProperty("user.dir") + File.separator + "res" + File.separator;
    public static final String ORIGINAL_CONFIG_FILE_PATH = RES_PATH + "startup_config.json";
    public static final String RESSOURCE_NAME = "IHM";
    public static final String COEFFICIENTS_CONFIG_FILE_PATH = RES_PATH + "coefficients_config.json";
    public static AssignmentPlatform platform = new AssignmentPlatform(new Teacher("", "", Set.of(RESSOURCE_NAME)));


    public static void importFilters(String path) {
        try {
            platform.setApprovalStatusByCriteriaFiltersFromJSON(path, RESSOURCE_NAME);
        } catch (RessourceNotFoundException | NotAllowedException | IOException | IncorrectFileFormatException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getMessage(), "Import des filtres d'approbation").show();
        }
    }

    public static void computeAssignments() {
        try {
            platform.computeAssignments(RESSOURCE_NAME);
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage(), "Calcul des affectations").show();
        }
    }

    public static Assignment<StudentTutor, StudentInDifficulty> getAssignment(Student s1, Student s2) {
        try {
            Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = platform.getAllAssignments(RESSOURCE_NAME);
            for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
                if (assignment.contains(s1, s2)) {
                    return assignment;
                }
            }
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return null;
    }

    public static AssignmentType getAssignmentType(Student s1, Student s2) {
        Assignment<StudentTutor, StudentInDifficulty> assignment = getAssignment(s1, s2);
        if (assignment == null)
            return null;
        return assignment.getType();
    }

    public static List<StudentTutor> getStudentsTutors() {
        List<StudentTutor> list = new ArrayList<>();
        try {
            list.addAll(platform.getStudentsTutor(RESSOURCE_NAME));
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return list;
    }

    public static List<StudentInDifficulty> getStudentsInDifficulty() {
        List<StudentInDifficulty> list = new ArrayList<>();
        try {
            list.addAll(platform.getStudentsInDifficulty(RESSOURCE_NAME));
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return list;
    }

    public static void addManualAssignment(StudentTutor studentTutor, StudentInDifficulty studentInDifficulty, boolean isForbidden) {
        try {
            if (isForbidden) {
                platform.addForbiddenAssignment(RESSOURCE_NAME, studentTutor, studentInDifficulty);
            } else {
                platform.addForcedAssignment(RESSOURCE_NAME, studentTutor, studentInDifficulty);
            }
        } catch (RessourceNotFoundException | StudentNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public static void importStudents(String path) {
        try {
            platform.importStudentsFromJSON(path);
        } catch (IOException | IncorrectFileFormatException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public static void removeAssignment(StudentTutor studentTutor, StudentInDifficulty studentInDifficulty) {
        try {
            platform.removeAssignment(RESSOURCE_NAME, studentTutor, studentInDifficulty);
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public static void resetAssignments() {
        try {
            platform.resetAssignments(RESSOURCE_NAME, AssignmentType.COMPUTED);
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public static List<Assignment<StudentTutor, StudentInDifficulty>> getEffectiveAssignments() {
        List<Assignment<StudentTutor, StudentInDifficulty>> list = new ArrayList<>();
        try {
            list.addAll(platform.getEffectiveAssignments(RESSOURCE_NAME));
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return list;
    }

    public static List<Assignment<StudentTutor, StudentInDifficulty>> getForbiddenAssignments() {
        List<Assignment<StudentTutor, StudentInDifficulty>> list = new ArrayList<>();
        try {
            list.addAll(platform.getForbiddenAssignments(RESSOURCE_NAME));
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return list;
    }

    public static void excludeStudent(Student student) {
        try {
            // Copy of the set to prevent concurrent modification exception
            Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = new HashSet<>(platform.getAllAssignments(RESSOURCE_NAME));
            for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
                if (assignment.contains(student)) {
                    platform.removeAssignment(RESSOURCE_NAME, assignment.getLeft(), assignment.getRight());
                }
            }
            student.setApprovalStatus(RESSOURCE_NAME, false);
        } catch (RessourceNotFoundException | NotAllowedException e) {
            new CustomAlert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public static void main(String[] args) {
        Main.launch(args);
    }

    public static void exportConfigToJSON(String path) {
        try {
            platform.exportCoefficientsToJSON(path);
        } catch (IOException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public static void importCoefficientFromJSON(String path) {
        try {
            platform.importCoefficientFromJSON(path);
        } catch (IOException | IncorrectFileFormatException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public static void setCriteriaCoefficient(ScoreCriteria scoreCriteria, double doubleValue) {
        platform.setCriteriaCoefficient(scoreCriteria, doubleValue);
    }

    public static Map<ScoreCriteria, Double> getCriteriaCoefficients() {
        return platform.getCriteriaCoefficients();
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/resources/fxml/GlobalWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            new CustomAlert(Alert.AlertType.ERROR, "Impossible de charger un composant graphique " + e).show();
        }
        primaryStage.setTitle("Calculateur d'affectations pour tutorat");
        primaryStage.show();

        try {
            platform.importConfigFromJSON(Main.ORIGINAL_CONFIG_FILE_PATH);
        } catch (IOException | IncorrectFileFormatException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getMessage(), "Import de la configuration").show();
        }

    }

}