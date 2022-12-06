package assignment.calculation;

public enum ScoreCriteria {
    OVERALL_GRADE(3, 0, 20, 1.4),
    RESSOURCE_OVERALL_GRADE(4, 0, 20, 1.6),
    TUTOR_YEAR(4, 2, 3),
    TUTOR_MOTIVATION(1, 0, 2),
    STUDENT_IN_DIFFICULTY_MOTIVATION(1, 0, 2);

    private final double defaultCoefficient;
    private final double min;
    private final double max;
    private final double amplifier;

    ScoreCriteria(double coefficient, double min, double max, double amplifier) {
        this.defaultCoefficient = coefficient;
        this.min = min;
        this.max = max;
        this.amplifier = amplifier;
    }

    ScoreCriteria(double coefficient, double min, double max) {
        this(coefficient, min, max, 1);
    }

    public double getDefaultCoefficient() {
        return this.defaultCoefficient;
    }

    public double getAmplifiedMin() {
        return Math.pow(this.min, this.amplifier);
    }

    public double getMin() {
        return this.min;
    }

    public double getAmplifiedMax() {
        return Math.pow(this.max, this.amplifier);
    }

    public double getMax() {
        return this.max;
    }

    public double getAmplifier() {
        return this.amplifier;
    }

}
