package team_10.client.data.source.local;

import android.support.annotation.NonNull;

import team_10.client.MainActivity;
import team_10.client.data.source.UserDataSource;
import team_10.client.utility.io.AppExecutors;

public class UserLocalDataSource implements UserDataSource {

    private static volatile UserLocalDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private final SharedPreferencesManager mSharedPreferencesManager;

    // Prevent direct instantiation.
    private UserLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull SharedPreferencesManager sharedPreferencesManager) {
        mAppExecutors = appExecutors;
        mSharedPreferencesManager = sharedPreferencesManager;

    }

    public static UserLocalDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (UserLocalDataSource.class) {
                if (INSTANCE == null) {

                    SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(MainActivity.myContext);

                    INSTANCE = new UserLocalDataSource(appExecutors, sharedPreferencesManager);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUserData(LoadUserDataCallback callback) {

    }

    @Override
    public void saveUserData(SaveUserDataCallback callback) {

    }

    @Override
    public void removeUserData() {

    }

    @Override
    public int getUserID() {
        return 0;
    }

    @Override
    public void setUserID(int userID) {

    }

    @Override
    public String getAuthenticationToken() {
        return null;
    }

    @Override
    public void setAuthenticationToken(String authenticationToken) {

    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public void setPassword() {

    }

    @Override
    public int getNumAccountsCreated() {
        return mSharedPreferencesManager.getNumberOfCreatedAccounts();
    }

    @Override
    public void setNumAccountsCreated(int n) {
        mSharedPreferencesManager.setNumberOfCreatedAccounts(n);
    }
}
