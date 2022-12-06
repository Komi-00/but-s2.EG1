package assignment.output;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

import assignment.calculation.ScoreCriteria;

public class configJSONWriter extends JSONWriter {
    public configJSONWriter(String path) {
        super(path);
    }

    public void writeCoefficients(Map<ScoreCriteria, Double> criteriaCoefficientsUpdate) throws IOException {
        JSONObject configJSONObject = new JSONObject();
        configJSONObject.put("coefficients", new JSONObject());
        JSONObject coefficientsJSONObject = configJSONObject.getJSONObject("coefficients");

        for (ScoreCriteria sc : criteriaCoefficientsUpdate.keySet()) {
            coefficientsJSONObject.put(sc.toString(), new JSONObject());
            coefficientsJSONObject.getJSONObject(sc.toString()).put("coefficient", criteriaCoefficientsUpdate.get(sc));
        }
        this.writeJSONObject(configJSONObject);
    }
}
