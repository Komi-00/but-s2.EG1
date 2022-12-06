package assignment.calculation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * We can't add multiple same vertex to a graph, so we need to wrap each unique
 * assignable into multiple wrapper objects if we want to add them by
 * duplication to the graph.
 * One other solution was to make a copy of the assignable object but we think
 * this way is better and much more usable.
 */
public class UniqueAssignableWrapper implements IAssignable {
    protected static Map<IAssignable, Integer> assignablesAddedCounter = new HashMap<>();
    private final IAssignable ASSIGNABLE;
    private final int ASSIGNABLE_COUNTER;

    public UniqueAssignableWrapper(IAssignable assignable) {
        assignablesAddedCounter.put(assignable, assignablesAddedCounter.getOrDefault(assignable, 0) + 1);
        this.ASSIGNABLE = assignable;
        this.ASSIGNABLE_COUNTER = assignablesAddedCounter.get(assignable);
    }

    public static void reset() {
        assignablesAddedCounter.clear();
    }

    public IAssignable getAssignable() {
        return this.ASSIGNABLE;
    }

    public int getNumberOfDuplications() {
        return this.ASSIGNABLE_COUNTER;
    }

    @Override
    public String toString() {
        return "Wrapped " + this.ASSIGNABLE.toString();
    }

    @Override
    public Map<ScoreCriteria, Double> getCriteriaValues(String ressourceName) {
        return this.ASSIGNABLE.getCriteriaValues(ressourceName);
    }

    @Override
    public boolean isFictive() {
        return this.ASSIGNABLE.isFictive();
    }

    @Override
    public boolean isApproved(String ressourceName) {
        return this.ASSIGNABLE.isApproved(ressourceName);
    }

    @Override
    public boolean canHaveMultiple() {
        return this.ASSIGNABLE.canHaveMultiple();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UniqueAssignableWrapper)) {
            return false;
        }
        UniqueAssignableWrapper uniqueAssignableWrapper = (UniqueAssignableWrapper) o;
        return Objects.equals(ASSIGNABLE, uniqueAssignableWrapper.ASSIGNABLE)
                && ASSIGNABLE_COUNTER == uniqueAssignableWrapper.ASSIGNABLE_COUNTER;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ASSIGNABLE, ASSIGNABLE_COUNTER);
    }

}
