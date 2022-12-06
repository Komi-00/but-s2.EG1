package gui.controllers;

import assignment.calculation.ScoreCriteria;
import gui.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML
    private Slider overallGradeSlider;
    @FXML
    private Slider ressourceOverallGradeSlider;
    @FXML
    private Slider tutorYearSlider;
    @FXML
    private Slider tutorMotivationSlider;
    @FXML
    private Slider studentInDifficultyMotivationSlider;

    public SettingsViewController() {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.handleResetButtonAction();
        overallGradeSlider.valueProperty().addListener(new SliderChangeHandler(ScoreCriteria.OVERALL_GRADE));
        ressourceOverallGradeSlider.valueProperty().addListener(new SliderChangeHandler(ScoreCriteria.RESSOURCE_OVERALL_GRADE));
        tutorYearSlider.valueProperty().addListener(new SliderChangeHandler(ScoreCriteria.TUTOR_YEAR));
        tutorMotivationSlider.valueProperty().addListener(new SliderChangeHandler(ScoreCriteria.TUTOR_MOTIVATION));
        studentInDifficultyMotivationSlider.valueProperty().addListener(new SliderChangeHandler(ScoreCriteria.STUDENT_IN_DIFFICULTY_MOTIVATION));
    }

    @FXML
    public void handleSaveButtonAction() {
        Main.exportConfigToJSON(Main.COEFFICIENTS_CONFIG_FILE_PATH);
    }

    @FXML
    public void handleResetButtonAction() {
        Main.importCoefficientFromJSON(Main.COEFFICIENTS_CONFIG_FILE_PATH);
        resetSliders();
    }

    public void resetSliders() {
        Map<ScoreCriteria, Double> criteriaCoefficients = Main.getCriteriaCoefficients();
        overallGradeSlider.valueProperty().set(criteriaCoefficients.get(ScoreCriteria.OVERALL_GRADE));
        ressourceOverallGradeSlider.valueProperty().set(criteriaCoefficients.get(ScoreCriteria.RESSOURCE_OVERALL_GRADE));
        tutorYearSlider.valueProperty().set(criteriaCoefficients.get(ScoreCriteria.TUTOR_YEAR));
        tutorMotivationSlider.valueProperty().set(criteriaCoefficients.get(ScoreCriteria.TUTOR_MOTIVATION));
        studentInDifficultyMotivationSlider.valueProperty().set(criteriaCoefficients.get(ScoreCriteria.STUDENT_IN_DIFFICULTY_MOTIVATION));
    }

    static class SliderChangeHandler implements ChangeListener<Number> {
        private final ScoreCriteria scoreCriteria;

        public SliderChangeHandler(ScoreCriteria scoreCriteria) {
            this.scoreCriteria = scoreCriteria;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Main.setCriteriaCoefficient(this.scoreCriteria, newValue.doubleValue());
        }
    }

}
