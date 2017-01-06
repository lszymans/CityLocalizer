package pl.lukaszszymansk.citylocalizer.androidtest;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.intent.Intents;
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
import pl.lukaszszymansk.citylocalizer.helpers.SharedPreferencesHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class SearchHistoryFragmentTest {

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
    public void testAddCity() throws Exception {
        changeSearchHistorySettings(false);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.history)).perform(click());

        checkCitiesList(0);
        goToHomeView();
        addCityHomeView();
        goToHistoryView();
        checkCitiesList(0);

        changeSearchHistorySettings(true);

        goToHomeView();
        addCityHomeView();
        goToHistoryView();
        checkCitiesList(1);
    }

    @Test
    public void testChooseCity() throws Exception {
        Intents.init();

        changeSearchHistorySettings(false);
        changeSearchHistorySettings(true);

        addCityHomeView();
        goToHistoryView();

        checkCitiesList(1);

        onData(anything()).inAdapterView(withId(R.id.lvHistory)).atPosition(0).perform(click());
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
    public void testDiscardSearchHistoryElement() throws Exception {
        changeSearchHistorySettings(false);
        changeSearchHistorySettings(true);

        addCityHomeView();
        goToHistoryView();

        checkCitiesList(1);
        onData(anything()).inAdapterView(withId(R.id.lvHistory))
                .atPosition(0)
                .onChildView(withId(R.id.ivDeleteItem))
                .perform(click());
        checkCitiesList(0);
    }

    @Test
    public void testOrientationChange() throws Exception {
        changeSearchHistorySettings(false);
        changeSearchHistorySettings(true);

        goToHistoryView();

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(0);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(0);

        goToHomeView();
        addCityHomeView();
        goToHistoryView();

        checkCitiesList(1);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
        checkCitiesList(1);
    }

    private void checkCitiesList(int expectedSize) throws Exception {
        boolean storeHistory = SharedPreferencesHelper.getInstance().ifStoreHistory();

        if (!storeHistory && expectedSize != 0) {
            throw new Exception("Wrong parameter value. Parameter expectedSize should be equal 0, when search history is disabled.");
        }

        if (expectedSize == 0) {
            onView(withId(R.id.tvEmptyHistoryList)).check(matches(isDisplayed()));
            onView(withId(R.id.lvHistory)).check(matches(not(isChecked())));
            onView(withId(R.id.lvHistory)).check(matches(ListMatcher.withListSize(0)));
        } else if (expectedSize > 0) {
            onView(withId(R.id.tvEmptyHistoryList)).check(matches(not(isChecked())));
            onView(withId(R.id.lvHistory)).check(matches(isDisplayed()));
            onView(withId(R.id.lvHistory)).check(matches(ListMatcher.withListSize(expectedSize)));
        } else {
            throw new Exception("Wrong parameter value. Parameter expectedSize should be grater or equal 0.");
        }
    }

    private void changeSearchHistorySettings(boolean expectedStoreHistory) throws Exception {
        boolean storeHistory = SharedPreferencesHelper.getInstance().ifStoreHistory();

        if (storeHistory != expectedStoreHistory) {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withText(R.string.settings)).perform(click());
            onView(withText(R.string.pref_title_store_history_on_device))
                    .perform(click());
            onView(isRoot()).perform(pressBack());

            storeHistory = SharedPreferencesHelper.getInstance().ifStoreHistory();
            if (storeHistory != expectedStoreHistory) {
                throw new Exception("Changing search history settings failed.");
            }
        }
    }

    private void goToHomeView() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.home)).perform(click());
    }

    private void goToHistoryView() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.history)).perform(click());
    }

    private void addCityHomeView() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(android.R.id.text1)).perform(replaceText(TESTED_CITY));
        onView(withId(android.R.id.button1)).perform(click());
    }
}