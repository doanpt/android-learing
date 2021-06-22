package musicplayer.group3.dev.musicplayer.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sev_user on 12/20/2016.
 */

public class SharePreferencesController {

    private static final String FILE_NAME = "setting_play_music";

    private static final SharePreferencesController preferencesManager = new SharePreferencesController();
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public static SharePreferencesController getInstance(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        return preferencesManager;
    }

    public String getString(String key, String value) {
        return mSharedPreferences.getString(key, value);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean value) {
        return mSharedPreferences.getBoolean(key, value);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public int getInt(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

}
