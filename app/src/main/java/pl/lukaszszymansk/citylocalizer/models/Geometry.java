package pl.lukaszszymansk.citylocalizer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Geometry implements Parcelable {

    @SerializedName("location")
    private Location location;

    @SerializedName("location_type")
    private String location_type;

    @SerializedName("viewport")
    private Area viewport;

    @SerializedName("bounds")
    private Area bounds;

    public Geometry(Parcel in) {
        location = (Location) in.readSerializable();
        location_type = in.readString();
        viewport = (Area) in.readSerializable();
        bounds = (Area) in.readSerializable();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationType() {
        return location_type;
    }

    public void setLocationType(String locationType) {
        this.location_type = locationType;
    }

    public Area getViewport() {
        return viewport;
    }

    public void setViewport(Area viewport) {
        this.viewport = viewport;
    }

    public Area getBounds() {
        return bounds;
    }

    public void setBounds(Area bounds) {
        this.bounds = bounds;
    }

    //---------------------------

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(location);
        out.writeString(location_type);
        out.writeSerializable(viewport);
        out.writeSerializable(bounds);
    }

    public static final Creator<Geometry> CREATOR
            = new Creator<Geometry>() {

        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}