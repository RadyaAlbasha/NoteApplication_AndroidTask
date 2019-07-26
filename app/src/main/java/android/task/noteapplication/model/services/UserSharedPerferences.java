package android.task.noteapplication.model.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserSharedPerferences {

   private SharedPreferences sharepreferences;

    public static UserSharedPerferences instance = null;

    public static UserSharedPerferences getInstance()
    {

        if (instance == null) {
            synchronized (UserSharedPerferences.class) {
                instance = new UserSharedPerferences();
            }
        }
        return instance;
    }
    public void saveISLogged_IN(Context context, Boolean isLoggedin) {
        sharepreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharepreferences.edit();
        editor.putBoolean("IS_LOGIN", isLoggedin);
        editor.commit();
    }

    public boolean getISLogged_IN(Context context) {
        sharepreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepreferences.getBoolean("IS_LOGIN", false);
    }

}
