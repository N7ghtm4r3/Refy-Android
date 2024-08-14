package com.tecknobit.refy.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.tecknobit.refycore.helpers.RefyLocalUser;

public class AndroidRefyLocalUser extends RefyLocalUser {

    private final SharedPreferences sharedPreferences;

    public AndroidRefyLocalUser(Context context) {
        this.sharedPreferences = context.getSharedPreferences(REFY_PREFERENCES_FILE, Context.MODE_PRIVATE);
        initLocalUser();
    }

    /**
     * Method to store and set a preference
     *
     * @param key   :   the key of the preference
     * @param value : the value of the preference
     */
    @Override
    protected void setPreference(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Method to get a stored preference
     *
     * @param key : the key of the preference to get
     * @return the preference stored as {@link String}
     */
    @Override
    protected String getPreference(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * Method to clear the current local user session <br>
     * No-any params required
     */
    @Override
    public void clear() {
        super.clear();
        sharedPreferences.edit().clear().apply();
    }

}
