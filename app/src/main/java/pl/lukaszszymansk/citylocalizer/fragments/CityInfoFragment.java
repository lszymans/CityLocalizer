package pl.lukaszszymansk.citylocalizer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.MapActivity;
import pl.lukaszszymansk.citylocalizer.core.BundleKeys;
import pl.lukaszszymansk.citylocalizer.models.AddressComponent;
import pl.lukaszszymansk.citylocalizer.models.Location;


public class CityInfoFragment extends Fragment {

    private static final String EXTRA_ADDRESS_COMPONENTS = "EXTRA_ADDRESS_COMPONENTS";

    public static CityInfoFragment newInstance(List<AddressComponent> addressComponent, Location location) {
        CityInfoFragment fragment = new CityInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_ADDRESS_COMPONENTS, (ArrayList<AddressComponent>)addressComponent);
        bundle.putSerializable(BundleKeys.EXTRA_CITY_LOCATION, location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_city_info, container, false);

        Bundle bundle = getArguments();

        if(bundle != null) {

            ArrayList<AddressComponent> addressComponents = bundle.getParcelableArrayList(EXTRA_ADDRESS_COMPONENTS);
            final Location location = (Location) bundle.getSerializable(BundleKeys.EXTRA_CITY_LOCATION);

            if (addressComponents != null) {
                TextView tvLocality = (TextView) rootView.findViewById(R.id.tvLocality);
                TextView tvCommunity = (TextView) rootView.findViewById(R.id.tvCommunity);
                TextView tvDistrict = (TextView) rootView.findViewById(R.id.tvDistrict);
                TextView tvProvince = (TextView) rootView.findViewById(R.id.tvProvince);
                TextView tvCountry = (TextView) rootView.findViewById(R.id.tvCountry);

                String locality = getResources().getString(R.string.locality) + ": " + getAddressComponent(addressComponents, "locality");
                tvLocality.setText(locality);

                String community = getResources().getString(R.string.community) + ": " + getAddressComponent(addressComponents, "administrative_area_level_3");
                tvCommunity.setText(community);

                String district = getResources().getString(R.string.district) + ": " + getAddressComponent(addressComponents, "administrative_area_level_2");
                tvDistrict.setText(district);

                String province = getResources().getString(R.string.province) + ": " + getAddressComponent(addressComponents, "administrative_area_level_1");
                tvProvince.setText(province);

                tvCountry.setText(getAddressComponent(addressComponents, "country"));
            }

            if(location != null) {
                ImageView ibViewLocation = (ImageView) rootView.findViewById(R.id.ivViewLocation);
                ibViewLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(MapActivity.getStartIntent(getActivity(), location));
                    }
                });
            }
        }

        return rootView;
    }

    private String getAddressComponent(ArrayList<AddressComponent> addressComponents, String firstType) {
        for (AddressComponent ac : addressComponents) {
            List<String> types = ac.getTypes();

            if (types != null && types.size()==2 && types.get(0).equals(firstType)
                    && types.get(1).equals("political"))
                return ac.getLongName();
        }

        return "";
    }
}
