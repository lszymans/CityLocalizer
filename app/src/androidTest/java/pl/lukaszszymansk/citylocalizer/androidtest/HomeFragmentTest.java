package pl.lukaszszymansk.citylocalizer.androidtest;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.CityInfoActivity;
import pl.lukaszszymansk.citylocalizer.activities.MainActivity;
import pl.lukaszszymansk.citylocalizer.androidtest.utils.ListMatcher;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private static final String TESTED_CITY = "Kraków";
    private static final String TESTED_CITY_LOCALITY = "Kraków";
    private static final String TESTED_CITY_COMMUNITY = "";
    private static final String TESTED_CITY_DISTRICT = "Kraków";
    private static final String TESTED_CITY_PROVINCE = "małopolskie";
    private static final String TESTED_CITY_COUNTRY = "Polska";

    @BeforeClass
    public static void setUp() throws Exception {
        Locale.setDefault(new Locale("pl"));
    }

    @Test
    public void testContentView() throws Exception {
        onView(ViewMatchers.withId(R.id.adView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddCity() throws Exception {
        checkCitiesList(0);

        onView(ViewMatchers.withId(R.id.fab)).perform(click());

        onView(withId(android.R.id.text1))
                .inRoot(isDialog())
                .check(matches(withText("")))
                .check(matches(isDisplayed()))
                .perform(replaceText(TESTED_CITY));

        onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .check(matches(withText(R.string.ok)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(R.string.cancel)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        checkCitiesList(1);
    }

    @Test
    public void testAddCityCancelled() throws Exception {
        checkCitiesList(0);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(android.R.id.text1))
                .inRoot(isDialog())
                .check(matches(withText("")))
                .check(matches(isDisplayed()))
                .perform(replaceText(TESTED_CITY));

        onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .check(matches(withText(R.string.ok)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(R.string.cancel)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button2)).perform(click());

        checkCitiesList(0);
    }

    @Test
    public void testChooseCity() throws Exception {
        Intents.init();

        addCityHomeView();

        checkCitiesList(1);

        onData(anything()).inAdapterView(withId(R.id.lvCities)).atPosition(0).perform(click());
        intended(hasComponent(CityInfoActivity.class.getName()));

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
        Intents.release();
    }

    @Test
    public void testOrientationChange() throws Exception {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(0);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(0);

        addCityHomeView();

        checkCitiesList(1);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(1);
    }

    private void checkCitiesList(int expectedSize) throws Exception {
        if (expectedSize == 0) {
            onView(withId(R.id.tvEmptyCitiesList)).check(matches(isDisplayed()));
            onView(withId(R.id.lvCities)).check(matches(not(isChecked())));
            onView(withId(R.id.lvCities)).check(matches(ListMatcher.withListSize(0)));
        } else if (expectedSize > 0) {
            onView(withId(R.id.tvEmptyCitiesList)).check(matches(not(isChecked())));
            onView(withId(R.id.lvCities)).check(matches(isDisplayed()));
            onView(withId(R.id.lvCities)).check(matches(ListMatcher.withListSize(expectedSize)));
        } else {
            throw new Exception("Wrong parameter value. Parameter expectedSize should be grater or equal 0.");
        }
    }

    private void addCityHomeView() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(android.R.id.text1)).perform(replaceText(TESTED_CITY));
        onView(withId(android.R.id.button1)).perform(click());
    }
}