package assignment.calculation;

import java.util.Objects;

public class Assignment<E1, E2> implements Comparable<Assignment<E1, E2>> {
    private final E1 LEFT;
    private final E2 RIGHT;
    private final AssignmentType ASSIGNMENT_TYPE;
    private final double SCORE;

    public Assignment(E1 left, E2 right, AssignmentType assignmentType, double score) {
        this.LEFT = left;
        this.RIGHT = right;
        this.ASSIGNMENT_TYPE = assignmentType;
        this.SCORE = score;
    }

    public Assignment(E1 left, E2 right, AssignmentType assignmentType) {
        this(left, right, assignmentType, -1);
    }

    public Assignment(E1 left, E2 right) {
        this(left, right, AssignmentType.COMPUTED);
    }

    public boolean containsLeft(E1 left) {
        return this.LEFT.equals(left);
    }

    public boolean containsRight(E2 right) {
        return this.RIGHT.equals(right);
    }

    public boolean contains(IAssignable assignable) {
        return this.RIGHT.equals(assignable) || this.LEFT.equals(assignable);
    }

    public boolean contains(IAssignable assignable1, IAssignable assignable2) {
        return this.contains(assignable1) && this.contains(assignable2);
    }

    public E1 getLeft() {
        return this.LEFT;
    }

    public E2 getRight() {
        return this.RIGHT;
    }

    public AssignmentType getType() {
        return ASSIGNMENT_TYPE;
    }

    public double getScore() {
        return this.SCORE;
    }

    public boolean isEffective() {
        return this.ASSIGNMENT_TYPE == AssignmentType.COMPUTED || this.ASSIGNMENT_TYPE == AssignmentType.FORCED;
    }

    public boolean isForbidden() {
        return this.ASSIGNMENT_TYPE == AssignmentType.FORBIDDEN;
    }

    public boolean isForced() {
        return this.ASSIGNMENT_TYPE == AssignmentType.FORCED;
    }

    public boolean isComputed() {
        return this.ASSIGNMENT_TYPE == AssignmentType.COMPUTED;
    }

    public String getScoreToString() {
        return this.isComputed() ? String.valueOf(this.SCORE) : "not computed";
    }

    @Override
    public String toString() {
        return this.getLeft() + " <-> " + this.getRight() + " (" + this.getType() + " - " + this.getScoreToString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Assignment)) {
            return false;
        }
        Assignment<E1, E2> assignment = (Assignment) o;
        return Objects.equals(LEFT, assignment.LEFT) && Objects.equals(RIGHT, assignment.RIGHT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(LEFT, RIGHT);
    }

    @Override
    public int compareTo(Assignment<E1, E2> o) {
        return (int) (this.SCORE - o.SCORE);
    }

}