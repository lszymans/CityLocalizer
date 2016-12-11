package pl.lukaszszymansk.citylocalizer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.fragments.CityInfoFragment;
import pl.lukaszszymansk.citylocalizer.models.AddressComponent;
import pl.lukaszszymansk.citylocalizer.models.GeocodingResult;
import pl.lukaszszymansk.citylocalizer.models.Location;


public class SlideViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GeocodingResult> geocodingResults;

    public SlideViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addAll(ArrayList<GeocodingResult> geocodingResults) {
        this.geocodingResults = geocodingResults;

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        List<AddressComponent> addressComponents = geocodingResults.get(position).getAddressComponents();
        Location location = geocodingResults.get(position).getGeometry().getLocation();

        return CityInfoFragment.newInstance(addressComponents, location);
    }

    @Override
    public int getCount() {
        if(geocodingResults != null)
            return geocodingResults.size();

        return 0;
    }
}
