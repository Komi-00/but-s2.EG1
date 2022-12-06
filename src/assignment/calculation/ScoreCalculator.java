package assignment.calculation;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScoreCalculator {
    public static final double SCORE_MAX = 100_000;
    public static final double SCORE_MIN = 0;
    public static final double SCORE_MALUS = 1_000;

    private static final Map<ScoreCriteria, Double> criteriaCoefficients = new EnumMap<>(ScoreCriteria.class);
    private final ICriteriaRatable criteriaRatable1;
    private final ICriteriaRatable criteriaRatable2;
    private final String ressourceName;

    static {
        resetCriteriaCoefficients();
    }

    /**
     * Create a ScoreCalculator for score calculation between two criteria ratables.
     *
     * @param criteriaRatable1 the first criteria ratable
     * @param criteriaRatable2 the second criteria ratable
     * @param ressourceName    the ressource name
     * @throws IllegalArgumentException
     */
    public ScoreCalculator(ICriteriaRatable criteriaRatable1, ICriteriaRatable criteriaRatable2, String ressourceName)
            throws IllegalArgumentException {

        if (criteriaRatable1.getCriteriaValues(ressourceName).containsValue(null)
                || criteriaRatable2.getCriteriaValues(ressourceName).containsValue(null)) {
            throw new IllegalArgumentException("can't use null values for criteria");
        }

        this.criteriaRatable1 = criteriaRatable1;
        this.criteriaRatable2 = criteriaRatable2;
        this.ressourceName = ressourceName;
    }

    /**
     * Get all criteria coefficients.
     */
    public static Map<ScoreCriteria, Double> getCriteriaCoefficients() {
        return criteriaCoefficients;
    }

    /**
     * Set a criteria coefficient
     *
     * @param criteria
     * @param coefficient
     */
    public static void setCriteriaCoefficient(ScoreCriteria criteria, double coefficient) {
        criteriaCoefficients.replace(criteria, coefficient);
    }

    /**
     * Replace some criteria coefficients by others criteria coefficients.
     */
    public static void setCriteriaCoefficients(Map<ScoreCriteria, Double> criteriaCoefficientsUpdate) {
        ScoreCalculator.criteriaCoefficients.putAll(criteriaCoefficientsUpdate);
    }

    /**
     * Reset all criteria coefficients
     */
    public static void resetCriteriaCoefficients() {
        for (ScoreCriteria criteria : ScoreCriteria.values()) {
            criteriaCoefficients.put(criteria, criteria.getDefaultCoefficient());
        }
    }

    /**
     * Return value amplified by value specified in ScoreCriteria and normalize it between [0, 1]
     *
     * @param value    the value to amplify and normalize
     * @param criteria the criteria to apply to the value
     * @return the value amplified and normalized according to criteria
     */
    private static double getAmplifiedNormalizedValue(double value, ScoreCriteria criteria) {
        double powerValue = Math.pow(value, criteria.getAmplifier());

        return (powerValue - criteria.getAmplifiedMin()) / (criteria.getAmplifiedMax() - criteria.getAmplifiedMin());
    }

    /**
     * Get the common score criteria between the two criteria ratables
     *
     * @return common criteria between the two criteria ratables
     */
    private Set<ScoreCriteria> getCommonCriteria() {
        // Only keep common criteria (intersection of both sets)
        Map<ScoreCriteria, Double> criteriaValues1 = criteriaRatable1.getCriteriaValues(ressourceName);
        Map<ScoreCriteria, Double> criteriaValues2 = criteriaRatable2.getCriteriaValues(ressourceName);

        Set<ScoreCriteria> commonCriteria = new HashSet<>(criteriaValues1.keySet());
        commonCriteria.retainAll(criteriaValues2.keySet());

        return commonCriteria;
    }

    /**
     * Compute the score for one common criteria between the two criteria ratables
     *
     * @param cr1Value the first criteria ratable
     * @param cr2Value the second criteria ratable
     * @param criteria the criteria to compute
     * @return the score computed for the criteria
     */
    private static double computeCommonCriteriaScore(double cr1Value, double cr2Value, ScoreCriteria criteria) {
        double criteriaCoefficient = criteriaCoefficients.get(criteria);

        // If the student in difficulty has the highest value (better value than the
        // tutor for the specified criteria), the result will be negative, so we admit
        // that if this happen, we use the value 0 to make the worst score possible
        double valuesDifference = Math
                .max(getAmplifiedNormalizedValue(cr1Value, criteria) - getAmplifiedNormalizedValue(cr2Value, criteria), 0.0);

        // We invert the value because the higher the score the worse it is (for
        // assignment algorithm)
        return (criteriaCoefficient - (valuesDifference * criteriaCoefficient));
    }

    /**
     * Compute the individual score for one criteria with one criteria ratable
     *
     * @param crValue  the criteria ratable
     * @param criteria the criteria to compute
     * @return the score computed for the criteria
     */
    private static double computeIndividualCriteriaScore(double crValue, ScoreCriteria criteria) {
        double criteriaCoefficient = criteriaCoefficients.get(criteria);

        return (criteriaCoefficient - (getAmplifiedNormalizedValue(crValue, criteria) * criteriaCoefficient));
    }

    /**
     * Compute the total individual score for one criteria ratable
     *
     * @param criteriaRatable the criteria ratable
     * @return the total individual score for the criteria ratable
     */
    private double computeIndividualScore(ICriteriaRatable criteriaRatable) {
        double score = 0;
        Set<ScoreCriteria> commonCriteria = this.getCommonCriteria();
        Map<ScoreCriteria, Double> criteriaValues = criteriaRatable.getCriteriaValues(ressourceName);

        for (Map.Entry<ScoreCriteria, Double> criteriaValue : criteriaValues.entrySet()) {
            if (!commonCriteria.contains(criteriaValue.getKey())) {
                score += computeIndividualCriteriaScore(criteriaValue.getValue(), criteriaValue.getKey());
            }
        }
        return score / (criteriaValues.size() - commonCriteria.size());
    }

    /**
     * Compute the total score for all common criteria between the two criteria ratables
     *
     * @return the total score for all common criteria between the two criteria ratables
     */
    private double computeCommonScore() {
        double score = 0;
        Set<ScoreCriteria> commonCriteria = this.getCommonCriteria();
        Map<ScoreCriteria, Double> criteriaValues1 = this.criteriaRatable1.getCriteriaValues(ressourceName);
        Map<ScoreCriteria, Double> criteriaValues2 = this.criteriaRatable2.getCriteriaValues(ressourceName);

        for (ScoreCriteria criteria : commonCriteria) {
            score += computeCommonCriteriaScore(criteriaValues1.get(criteria), criteriaValues2.get(criteria), criteria);
        }
        return score / commonCriteria.size();
    }

    /**
     * Compute overall score for the criteria ratable (common score between the two criteria ratables + individuals scores of the first and second criteria
     * ratables). Depending on the assignment type, we can either return the computed score or an arbitrary score
     *
     * @param calculationType the calculation type
     * @param malus           the malus to apply to the score
     * @return the computed overall score (or fixed score depending on {@link AssignmentType} between the two criteria ratables
     */
    public double computeOverallScore(AssignmentType calculationType, int malus) {
        if (calculationType == AssignmentType.FORBIDDEN) {
            return SCORE_MAX;
        } else if (calculationType == AssignmentType.FORCED) {
            return SCORE_MIN;
        }

        double overallScore = this.computeIndividualScore(this.criteriaRatable1) + this.computeIndividualScore(this.criteriaRatable2)
                + this.computeCommonScore();

        return overallScore + SCORE_MALUS * malus;
    }

    /**
     * Same as {@link #computeOverallScore(AssignmentType, int)} but the score between the two criteria ratables is always computed and there is no malus
     *
     * @return the computed overall score between the two criteria ratables
     */
    public double computeOverallScore() {
        return this.computeOverallScore(AssignmentType.COMPUTED, 0);
    }

}