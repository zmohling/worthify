package team_10.client.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import team_10.client.data.models.Account;

public interface AccountsDataSource {

    interface LoadAccountsCallback {

        void onAccountsLoaded(List<Account> accounts);

        void onDataNotAvailable();
    }

    interface GetAccountCallback {

        void onAccountLoaded(Account account);

        void onDataNotAvailable();
    }

    interface SaveAccountCallback {

        void onAccountSaved();

        void onServerNotAvailable();
    }

    interface RefreshAccountsCallback {

        void onAccountsRefreshed(List<Account> accounts);

        void onServerNotAvailable();
    }

    void getAccounts(@NonNull LoadAccountsCallback callback);

    void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback);

    void saveAccount(@NonNull Account account, @NonNull SaveAccountCallback callback); // will stomp on accounts with same ID

    void disableAccount(@NonNull String accountID);

    void deleteAllAccounts();

    void refreshAccounts(@NonNull RefreshAccountsCallback callback);
}
