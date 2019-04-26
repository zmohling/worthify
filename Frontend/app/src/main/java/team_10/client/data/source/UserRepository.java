package team_10.client.data.source;

import android.support.annotation.NonNull;

import java.util.Map;

import team_10.client.data.User;
import team_10.client.data.models.Account;
import team_10.client.data.source.local.UserLocalDataSource;
import team_10.client.data.source.remote.UserRemoteDataSource;
import team_10.client.utility.io.AppExecutors;

public class UserRepository implements UserDataSource {

    private static UserRepository INSTANCE = null;

    private final UserDataSource mUserRemoteDataSource;

    private final UserDataSource mUserLocalDataSource;

    Map<String, Account> mCachedAccounts;

    boolean mCacheIsDirty = false;

    // Private constructor for Singleton pattern
    private UserRepository(@NonNull UserDataSource userRemoteDataSource,
                           @NonNull UserDataSource userLocalDataSource) {
        mUserRemoteDataSource = userRemoteDataSource;
        mUserLocalDataSource = userLocalDataSource;
    }

    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            AppExecutors appExecutors = new AppExecutors();

            UserLocalDataSource userLocalDataSource = UserLocalDataSource.getInstance(appExecutors);
            UserRemoteDataSource userRemoteDataSource = UserRemoteDataSource.getInstance(appExecutors);

            INSTANCE = new UserRepository(userRemoteDataSource, userLocalDataSource);
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getUserData(LoadUserDataCallback callback) {

    }

    @Override
    public void saveUserData(SaveUserDataCallback callback) {

    }

    @Override
    public void removeUserData() {
        mUserLocalDataSource.removeUserData();
    }

    @Override
    public int getUserID() {
        return User.getID();
    }

    @Override
    public void setUserID(int userID) {
    }

    @Override
    public String getAuthenticationToken() {
        return mUserLocalDataSource.getAuthenticationToken();
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
        return mUserLocalDataSource.getNumAccountsCreated();
    }

    @Override
    public void setNumAccountsCreated(int n) {
        mUserLocalDataSource.setNumAccountsCreated(n);
    }
}
