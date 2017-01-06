package pl.lukaszszymansk.citylocalizer.androidtest.utils;

import android.view.View;
import android.widget.AdapterView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ListMatcher {

    public static Matcher<View> withListSize(final int expectedSize) {

        return new TypeSafeMatcher<View>() {
            int actualSize;

            @Override public boolean matchesSafely(final View view) {
                actualSize = ((AdapterView) view).getAdapter().getCount();

                return actualSize == expectedSize;
            }

            @Override public void describeTo(final Description description) {
                description.appendText("ListView expected size is " + expectedSize + ", the actual size is " + actualSize);
            }
        };
    }
}