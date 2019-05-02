package team_10.client.data.source;

import java.util.List;

import team_10.client.data.models.Account;

public interface UserDataSource {

    interface LoadUserDataCallback {

        void onSettingsLoaded(List<Account> accounts);

        void onDataNotAvailable();

        void onAuthenticationError();
    }

    interface SaveUserDataCallback {

        void onAccountSaved(Account account);

        void onServerNotAvailable();

        void onAuthenticationError();
    }

    void getUserData(LoadUserDataCallback callback);

    void saveUserData(SaveUserDataCallback callback);

    void removeUserData();

    int getUserID();

    void setUserID(int userID);

    String getAuthenticationToken();

    void setAuthenticationToken(String authenticationToken);

    String getFirstName();

    void setFirstName(String firstName);

    String getEmail();

    void setEmail(String email);

    void setPassword();

    int getNumAccountsCreated();

    void setNumAccountsCreated(int n);
}
