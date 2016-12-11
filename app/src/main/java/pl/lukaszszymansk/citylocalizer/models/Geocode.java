package pl.lukaszszymansk.citylocalizer.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Geocode {

    @SerializedName("results")
    private List<GeocodingResult> results = new ArrayList<GeocodingResult>();

    @SerializedName("status")
    private String status;

    public List<GeocodingResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<GeocodingResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodingResult> getResultsForCities() {

        List<GeocodingResult> geocodingResults = new ArrayList<GeocodingResult>();

        for(GeocodingResult cb : results) {
            List<String> types = cb.getTypes();
            if (types != null && types.size()==2 && types.get(0).equals("locality")
                    && types.get(1).equals("political"))
                geocodingResults.add(cb);
        }
        return geocodingResults;
    }

}
