package assignment.tutoring;

public class CriteriaFilter {
    private final FilterCriteriaType FILTER_CRITERIA_TYPE;
    private final double THRESHOLD;
    private final boolean SHALL_APPROVE;

    public CriteriaFilter(FilterCriteriaType filterCriteriaType, double threshold, boolean shallApprove) {
        this.FILTER_CRITERIA_TYPE = filterCriteriaType;
        this.THRESHOLD = threshold;
        this.SHALL_APPROVE = shallApprove;
    }

    public FilterCriteriaType getFilterCriteriaType() {
        return FILTER_CRITERIA_TYPE;
    }

    public double getThreshold() {
        return THRESHOLD;
    }

    public boolean shallApprove() {
        return this.SHALL_APPROVE;
    }

    @Override
    public String toString() {
        return "CriteriaFilter [approvement=" + this.SHALL_APPROVE + ", filterCriteriaType=" + this.FILTER_CRITERIA_TYPE + ", threshold="
                + this.THRESHOLD + "]";
    }

}
