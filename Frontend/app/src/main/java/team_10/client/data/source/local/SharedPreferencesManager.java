package team_10.client.data.source.local;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import team_10.client.login_and_registration.LoginActivity;
import team_10.client.data.User;

public class SharedPreferencesManager {
    private static final String SHARED_PREF_NAME = "networthsharedpref";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_LASTNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";
    private static final String KEY_TYPE = "keytype";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_NUM_ACCOUNTS = "keynumaccounts";

    private static SharedPreferencesManager mInstance;
    private static Context mCtx;

    private SharedPreferencesManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getID());
        editor.putString(KEY_LASTNAME, user.getLastName());
        editor.putString(KEY_FIRSTNAME, user.getFirstName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_TYPE, user.getType());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.apply();
    }

    public int getNumberOfCreatedAccounts() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_NUM_ACCOUNTS, 0);
    }

    public void setNumberOfCreatedAccounts(int n) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_NUM_ACCOUNTS, n);
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public static User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_LASTNAME, null),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getInt(KEY_TYPE, -1),
                sharedPreferences.getString(KEY_TOKEN, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
