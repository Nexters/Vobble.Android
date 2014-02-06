package com.nexters.vobble.common;

import android.content.Context;
import android.content.SharedPreferences;

public class VobblePreferencesManager {
    private final static String PREF_KEY = "VOBBLE";
    private final static String PREF_USER_ID = "VOBBLE_USER_ID";
    private final static String PREF_TOKEN = "VOBBLE_TOKEN";

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public VobblePreferencesManager(Context context) {
        sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setUserId(int userId) {
        editor.putInt(PREF_USER_ID, userId);
        editor.commit();
    }

    public void setToken(String token) {
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public int getUserId() {
        return sharedPref.getInt(PREF_USER_ID, -1);
    }

    public String getToken() {
        return sharedPref.getString(PREF_TOKEN, "");
    }
}
