package assignment.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import assignment.tutoring.CriteriaFilter;
import assignment.tutoring.FilterCriteriaType;

public class FiltersJSONReader extends JSONReader {
    public FiltersJSONReader(String path) {
        super(path);
    }

    private List<CriteriaFilter> readCriteriaFilters(String key) throws IOException, IncorrectFileFormatException {
        List<CriteriaFilter> criteriaFiltersList = new ArrayList<>();

        try {
            JSONObject criteriaFilterJSONObject = readJSONObject(key);
            JSONObject approveJSONObject = criteriaFilterJSONObject.getJSONObject("approve");
            JSONObject rejectJSONObject = criteriaFilterJSONObject.getJSONObject("reject");

            for (String criteriaTypeName : rejectJSONObject.keySet()) {
                CriteriaFilter criteriaFilter = new CriteriaFilter(FilterCriteriaType.valueOf(criteriaTypeName),
                        rejectJSONObject.getDouble(criteriaTypeName), false);
                criteriaFiltersList.add(criteriaFilter);
            }

            for (String criteriaTypeName : approveJSONObject.keySet()) {
                CriteriaFilter criteriaFilter = new CriteriaFilter(FilterCriteriaType.valueOf(criteriaTypeName),
                        approveJSONObject.getDouble(criteriaTypeName), true);
                criteriaFiltersList.add(criteriaFilter);
            }

        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }

        return criteriaFiltersList;

    }

    public List<CriteriaFilter> readCriteriaFiltersStudentsTutor() throws IOException, IncorrectFileFormatException {
        return readCriteriaFilters("students_tutor");
    }

    public List<CriteriaFilter> readCriteriaFiltersStudentsInDifficulty() throws IOException, IncorrectFileFormatException {
        return readCriteriaFilters("students_in_difficulty");
    }
}
