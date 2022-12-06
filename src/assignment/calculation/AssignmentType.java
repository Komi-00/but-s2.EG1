package assignment.calculation;

public enum AssignmentType {
    FORCED, FORBIDDEN, COMPUTED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}