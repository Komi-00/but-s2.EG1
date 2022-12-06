package assignment.calculation;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class FictiveAssignable implements IAssignable {
    private static int counter = 0;
    private final int ID;

    public FictiveAssignable() {
        this.ID = counter;
        counter += 1;
    }

    @Override
    public String toString() {
        return "FictiveAssignable " + this.ID;
    }

    @Override
    public Map<ScoreCriteria, Double> getCriteriaValues(String ressourceName) {
        return new EnumMap<>(ScoreCriteria.class);
    }

    @Override
    public boolean isFictive() {
        return true;
    }

    @Override
    public boolean canHaveMultiple() {
        return false;
    }

    @Override
    public boolean isApproved(String ressourceName) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FictiveAssignable)) {
            return false;
        }
        FictiveAssignable fictiveStudent = (FictiveAssignable) o;
        return ID == fictiveStudent.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

}
