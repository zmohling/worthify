package team_10.client.data.source.remote;

import android.support.annotation.NonNull;

import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;

public class AccountsRemoteDataSource implements AccountsDataSource {
    @Override
    public void getAccounts(@NonNull LoadAccountsCallback callback) {

    }

    @Override
    public void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void saveAccount(@NonNull Account account, @NonNull SaveAccountCallback callback) {

    }

    @Override
    public void disableAccount(@NonNull String accountID) {

    }

    @Override
    public void deleteAllAccounts() {

    }

    @Override
    public void refreshAccounts(@NonNull RefreshAccountsCallback callback) {

    }
}
