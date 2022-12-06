package assignment.input;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import assignment.calculation.ScoreCriteria;

public class ConfigJSONReader extends JSONReader {

    public ConfigJSONReader(String path) {
        super(path);
    }

    public Map<ScoreCriteria, Double> readCoefficients() throws IOException, IncorrectFileFormatException {
        Map<ScoreCriteria, Double> criteriaCoefficients = new EnumMap<>(ScoreCriteria.class);
        try {
            JSONObject coefficientsJSONObject = readJSONObject("coefficients");

            for (String key : coefficientsJSONObject.keySet()) {
                JSONObject obj = coefficientsJSONObject.getJSONObject(key);
                criteriaCoefficients.put(ScoreCriteria.valueOf(key), obj.getDouble("coefficient"));
            }
        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }

        return criteriaCoefficients;
    }

    public Map<String, Integer> readRessources() throws IOException, IncorrectFileFormatException {
        Map<String, Integer> ressources = new HashMap<>();
        try {
            JSONObject ressourcesJSONObject = readJSONObject("ressources");
            for (String key : ressourcesJSONObject.keySet()) {
                ressources.put(key, ressourcesJSONObject.getInt(key));
            }
        } catch (JSONException | IllegalArgumentException e) {
            throw new IncorrectFileFormatException(this.getPath());
        }
        return ressources;
    }
}
