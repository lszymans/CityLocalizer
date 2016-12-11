package pl.lukaszszymansk.citylocalizer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.adapters.SlideViewPagerAdapter;
import pl.lukaszszymansk.citylocalizer.models.GeocodingResult;


public class CityInfoActivity extends AppCompatActivity {

    private static final String EXTRA_GEOCODING_RESULTS = "EXTRA_GEOCODING_RESULTS";

    public static Intent getStartIntent(Context context, List<GeocodingResult>  geocodingResults) {
        Intent intent = new Intent(context, CityInfoActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GEOCODING_RESULTS, (ArrayList<GeocodingResult>) geocodingResults);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);

        overridePendingTransition(R.anim.from_right, R.anim.empty);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        SlideViewPagerAdapter pagerAdapter = new SlideViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        CirclePageIndicator cpiIndicator = (CirclePageIndicator) findViewById(R.id.cpiIndicator);
        cpiIndicator.setViewPager(viewPager);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            ArrayList<GeocodingResult> geocodingResults = bundle.getParcelableArrayList(EXTRA_GEOCODING_RESULTS);
            pagerAdapter.addAll(geocodingResults);
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
        overridePendingTransition(R.anim.empty, R.anim.from_left);
    }
}
