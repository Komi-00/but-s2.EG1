package assignment.calculation;

public interface IAssignable extends ICriteriaRatable {
    public boolean isFictive();

    public boolean canHaveMultiple();

    public boolean isApproved(String ressourceName);
}
