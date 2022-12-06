package assignment.calculation;

import java.util.Map;

public interface ICriteriaRatable {
    Map<ScoreCriteria, Double> getCriteriaValues(String ressourceName);
}
