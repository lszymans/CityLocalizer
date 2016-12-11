package pl.lukaszszymansk.citylocalizer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.core.BundleKeys;
import pl.lukaszszymansk.citylocalizer.models.Location;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static Intent getStartIntent(Context context, Location location) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(BundleKeys.EXTRA_CITY_LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        overridePendingTransition(R.anim.slide_up, R.anim.empty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Location location = (Location) bundle.getSerializable(BundleKeys.EXTRA_CITY_LOCATION);

            LatLng cityCoordinates = new LatLng(location.getLat(), location.getLng());
            String markerTitle = UtilsService.getLocalization(cityCoordinates);

            mMap.addMarker(new MarkerOptions().position(cityCoordinates).title(markerTitle));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityCoordinates, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.empty, R.anim.slide_down);
    }
}
