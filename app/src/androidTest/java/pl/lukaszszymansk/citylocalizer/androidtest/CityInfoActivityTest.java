package pl.lukaszszymansk.citylocalizer.androidtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.GsonBuilder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.CityInfoActivity;
import pl.lukaszszymansk.citylocalizer.activities.MapActivity;
import pl.lukaszszymansk.citylocalizer.core.BundleKeys;
import pl.lukaszszymansk.citylocalizer.models.Geocode;
import pl.lukaszszymansk.citylocalizer.models.GeocodingResult;
import pl.lukaszszymansk.citylocalizer.models.Location;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class CityInfoActivityTest {

    private static final String TESTED_CITY_LOCALITY = "Miami";
    private static final String TESTED_CITY_COMMUNITY = "";
    private static final String TESTED_CITY_DISTRICT = "Hrabstwo Miami-Dade";
    private static final String TESTED_CITY_PROVINCE = "Floryda";
    private static final String TESTED_CITY_COUNTRY = "Stany Zjednoczone";
    private static final String JSON_OUTPUT = "{   \"results\" : [      {         \"address_components\" : [            {               \"long_name\" : \"Miami\",               \"short_name\" : \"Miami\",               \"types\" : [ \"locality\", \"political\" ]            },            {               \"long_name\" : \"Hrabstwo Miami-Dade\",               \"short_name\" : \"Hrabstwo Miami-Dade\",               \"types\" : [ \"administrative_area_level_2\", \"political\" ]            },            {               \"long_name\" : \"Floryda\",               \"short_name\" : \"FL\",               \"types\" : [ \"administrative_area_level_1\", \"political\" ]            },            {               \"long_name\" : \"Stany Zjednoczone\",               \"short_name\" : \"US\",               \"types\" : [ \"country\", \"political\" ]            }         ],         \"formatted_address\" : \"Miami, Floryda, Stany Zjednoczone\",         \"geometry\" : {            \"bounds\" : {               \"northeast\" : {                  \"lat\" : 25.855773,                  \"lng\" : -80.139157               },               \"southwest\" : {                  \"lat\" : 25.709042,                  \"lng\" : -80.31975989999999               }            },            \"location\" : {               \"lat\" : 25.7616798,               \"lng\" : -80.1917902            },            \"location_type\" : \"APPROXIMATE\",            \"viewport\" : {               \"northeast\" : {                  \"lat\" : 25.8556059,                  \"lng\" : -80.14240029999999               },               \"southwest\" : {                  \"lat\" : 25.709042,                  \"lng\" : -80.3196064               }            }         },         \"place_id\" : \"ChIJEcHIDqKw2YgRZU-t3XHylv8\",         \"types\" : [ \"locality\", \"political\" ]      }   ],   \"status\" : \"OK\"}";

    @Rule
    public ActivityTestRule<CityInfoActivity> mActivityRule = new ActivityTestRule<CityInfoActivity>(CityInfoActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Geocode geocode = new GsonBuilder().create().fromJson(JSON_OUTPUT, Geocode.class);
            List<GeocodingResult> geocodingResults = geocode.getResultsForCities();

            return CityInfoActivity.getStartIntent(targetContext, geocodingResults);
        }
    };

    @Test
    public void testContentView() throws Exception {
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        String locality = resources.getString(R.string.locality) + ": " + TESTED_CITY_LOCALITY;
        String community = resources.getString(R.string.community) + ": " + TESTED_CITY_COMMUNITY;
        String district = resources.getString(R.string.district) + ": " + TESTED_CITY_DISTRICT;
        String province = resources.getString(R.string.province) + ": " + TESTED_CITY_PROVINCE;

        onView(withId(R.id.tvLocality)).check(matches(withText(locality)));
        onView(withId(R.id.tvCommunity)).check(matches(withText(community)));
        onView(withId(R.id.tvDistrict)).check(matches(withText(district)));
        onView(withId(R.id.tvProvince)).check(matches(withText(province)));
        onView(withId(R.id.tvCountry)).check(matches(withText(TESTED_CITY_COUNTRY)));
        onView(withId(R.id.ivViewLocation)).check(matches(isDisplayed()));
    }

    @Test
    public void testLocationButton() throws Exception {
        Intents.init();

        onView(withId(R.id.ivViewLocation)).perform(click());

        intended(allOf(hasComponent(MapActivity.class.getName()),
                hasExtra(equalTo(BundleKeys.EXTRA_CITY_LOCATION), equalTo(new Location(25.7616798, -80.1917902)))));

        Intents.release();
    }
}
