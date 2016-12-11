package pl.lukaszszymansk.citylocalizer.services;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;

public class UtilsService {

    public static String getAppVersion(Context context) {
        String versionText = "";
        try {
            versionText = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionText;
    }

    public static void closeKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swapControls(Activity activity, int controlToShowId, int controlToHideId) {
        final View controlToShow = activity.findViewById(controlToShowId);
        final View controlToHide = activity.findViewById(controlToHideId);

        int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

        controlToShow.setVisibility(View.VISIBLE);
        controlToShow.animate().setDuration(shortAnimTime).alpha(1)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                controlToShow.setVisibility(View.VISIBLE);
            }
        });

        controlToHide.setVisibility(View.GONE);
        controlToHide.animate().setDuration(shortAnimTime).alpha(0)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                controlToHide.setVisibility(View.GONE);
            }
        });
    }

    public static String getLocalization(LatLng point) {
        return String.format("(%s, %s)", String.valueOf(point.latitude), String.valueOf(point.longitude));
    }
}
