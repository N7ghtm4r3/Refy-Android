package com.tecknobit.refy.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.tecknobit.equinox.environment.records.EquinoxLocalUser;
import com.tecknobit.refycore.helpers.RefyLocalUser;
import com.tecknobit.refycore.records.RefyUser;

/**
 * The {@code AndroidRefyLocalUser} class is useful to represent a {@link RefyUser} in the mobile
 * applications
 *
 * @see EquinoxLocalUser
 * @see RefyLocalUser
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class AndroidRefyLocalUser extends RefyLocalUser {

    /**
     * {@code preferences} the manager of the local preferences
     */
    private final SharedPreferences preferences;

    /**
     * Constructor to init {@link AndroidRefyLocalUser} class
     *
     * @param context: the context where the local user has been instantiated
     */
    public AndroidRefyLocalUser(Context context) {
        this.preferences = context.getSharedPreferences(REFY_PREFERENCES_FILE, Context.MODE_PRIVATE);
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
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Method to get a stored preference
     *
     * @param key : the key of the preference to get
     * @return the preference stored as {@link String}
     */
    @Override
    protected String getPreference(String key) {
        return preferences.getString(key, null);
    }

    /**
     * Method to clear the current local user session <br>
     * No-any params required
     */
    @Override
    public void clear() {
        super.clear();
        preferences.edit().clear().apply();
    }

}
