package pl.lukaszszymansk.citylocalizer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GeocodingResult implements Parcelable {

    @SerializedName("address_components")
    private List<AddressComponent> address_components = new ArrayList<AddressComponent>();

    @SerializedName("formatted_address")
    private String formatted_address;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("types")
    private List<String> types = new ArrayList<String>();

    public GeocodingResult(Parcel in) {
        address_components = in.readArrayList(AddressComponent.class.getClassLoader());
        formatted_address = in.readString();
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        place_id = in.readString();
        in.readStringList(types);
    }

    public List<AddressComponent> getAddressComponents() {
        return address_components;
    }

    public void setAddressComponents(List<AddressComponent> address_components) {
        this.address_components = address_components;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public void setFormattedAddress(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    //---------------------------

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(address_components);
        out.writeString(formatted_address);
        out.writeParcelable(geometry, flags);
        out.writeString(place_id);
        out.writeStringList(types);
    }

    public static final Creator<GeocodingResult> CREATOR
            = new Creator<GeocodingResult>() {

        public GeocodingResult createFromParcel(Parcel in) {
            return new GeocodingResult(in);
        }

        public GeocodingResult[] newArray(int size) {
            return new GeocodingResult[size];
        }
    };
}
