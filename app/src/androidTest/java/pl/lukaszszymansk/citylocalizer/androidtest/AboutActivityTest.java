package pl.lukaszszymansk.citylocalizer.androidtest;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.AboutActivity;
import pl.lukaszszymansk.citylocalizer.activities.WebPageActivity;
import pl.lukaszszymansk.citylocalizer.core.Settings;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
import static org.hamcrest.Matchers.isEmptyOrNullString;


@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {
    private static final String EXTRA_WEBPAGE_URL = "EXTRA_WEBPAGE_URL";
    private static final String EXTRA_WEBPAGE_CONTENT = "EXTRA_WEBPAGE_CONTENT";
    private static final String EXTRA_WEBPAGE_TITLE = "EXTRA_WEBPAGE_TITLE";

    @Rule
    public ActivityTestRule<AboutActivity> mActivityRule = new ActivityTestRule<>(AboutActivity.class);

    @Test
    public void testContentView() throws Exception {
        onView(withId(R.id.tvCompanyName)).check(matches(withText(R.string.company_name)));
        onView(withId(R.id.tvCompanyMail)).check(matches(withText(R.string.company_email)));
        onView(withId(R.id.ivAppLogo)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAppName)).check(matches(withText(R.string.app_name)));

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        String versionText = UtilsService.getAppVersion(getInstrumentation().getTargetContext());
        versionText = String.format("%s: %s", resources.getString(R.string.app_version), versionText);
        onView(withId(R.id.tvAppVersion)).check(matches(withText(versionText)));

        onView(withId(R.id.tvOpenSourceLicences)).check(matches(withText(R.string.app_licences)));
    }

    @Test
    public void testLicencesButton() throws Exception {
        Intents.init();

        String title = getInstrumentation().getTargetContext().getString(R.string.app_licences);

        onView(withId(R.id.tvOpenSourceLicences)).perform(click());

        intended(allOf(hasComponent(WebPageActivity.class.getName()),
                hasExtra(equalTo(EXTRA_WEBPAGE_URL), equalTo(Settings.LICENCES_URL)),
                hasExtra(equalTo(EXTRA_WEBPAGE_TITLE), equalTo(title)),
                hasExtra(equalTo(EXTRA_WEBPAGE_CONTENT), isEmptyOrNullString())));

        Intents.release();
    }
}
