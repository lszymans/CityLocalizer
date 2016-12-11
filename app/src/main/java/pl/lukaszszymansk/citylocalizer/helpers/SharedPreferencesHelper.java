package pl.lukaszszymansk.citylocalizer.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.lukaszszymansk.citylocalizer.core.BundleKeys;
import pl.lukaszszymansk.citylocalizer.core.MyApplication;

public class SharedPreferencesHelper {

    private static SharedPreferences mPreferences;
    private static SharedPreferencesHelper mInstance;
//    private static Editor mEditor;

    private SharedPreferencesHelper() {
        Context context = MyApplication.getAppContext();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        mEditor = mPreferences.edit();
    }

    public static SharedPreferencesHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPreferencesHelper();
        }
        return mInstance;
    }

    public Boolean ifStoreHistory() {
        return mPreferences.getBoolean(BundleKeys.KEY_PREF_STORE_HISTORY, true);
    }

//    public void putStoreHistory(Boolean value) {
//        mEditor.putBoolean(KEY_PREF_STORE_HISTORY, value)
//                .commit();
//    }
}
