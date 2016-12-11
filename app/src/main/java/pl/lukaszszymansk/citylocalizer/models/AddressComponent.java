package pl.lukaszszymansk.citylocalizer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddressComponent implements Parcelable {

    @SerializedName("long_name")
    private String long_name;

    @SerializedName("short_name")
    private String short_name;

    @SerializedName("types")
    private List<String> types = new ArrayList<String>();

    public AddressComponent(Parcel in) {
        long_name = in.readString();
        short_name = in.readString();
        in.readStringList(types);
    }

    public String getLongName() {
        return long_name;
    }

    public void setLongName(String long_name) {
        this.long_name = long_name;
    }

    public String getShortName() {
        return short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
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
        out.writeString(long_name);
        out.writeString(short_name);
        out.writeStringList(types);
    }

    public static final Creator<AddressComponent> CREATOR
            = new Creator<AddressComponent>() {

        public AddressComponent createFromParcel(Parcel in) {
            return new AddressComponent(in);
        }

        public AddressComponent[] newArray(int size) {
            return new AddressComponent[size];
        }
    };
}
