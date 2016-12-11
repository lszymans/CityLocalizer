package pl.lukaszszymansk.citylocalizer.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Serializable {

    @SerializedName("southwest")
    private Location southwest;

    @SerializedName("northeast")
    private Location northeast;

    public Location getSouthWest() {
        return southwest;
    }

    public void setSouthWest(Location southWest) {
        this.southwest = southWest;
    }

    public Location getNorthEast() {
        return northeast;
    }

    public void setNorthEast(Location northEast) {
        this.northeast = northEast;
    }
}
