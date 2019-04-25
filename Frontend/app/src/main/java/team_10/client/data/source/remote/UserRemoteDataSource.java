package team_10.client.data.source.remote;

import android.support.annotation.NonNull;

import team_10.client.data.source.UserDataSource;
import team_10.client.utility.AppExecutors;

public class UserRemoteDataSource implements UserDataSource {

    private static volatile UserRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private UserRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static UserRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (UserRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRemoteDataSource(appExecutors);
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
    public String getUserID() {
        return null;
    }

    @Override
    public void setUserID(String userID) {

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
        return 0;
    }
}
