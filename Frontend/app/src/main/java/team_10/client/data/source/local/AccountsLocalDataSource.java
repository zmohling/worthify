package team_10.client.data.source.local;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import team_10.client.activity.MainActivity;
import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.data.source.AccountsRepository;
import team_10.client.utility.AppExecutors;
import team_10.client.utility.IO;

public class AccountsLocalDataSource implements AccountsDataSource {

    private static volatile AccountsLocalDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private AccountsLocalDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static AccountsLocalDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (AccountsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AccountsLocalDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getAccounts(@NonNull LoadAccountsCallback callback) {
        Runnable readAccountsFromFile = new Runnable() {
            @Override
            public void run() {

                final List<Account> accounts = new ArrayList<>();

                String accountsStringFromFile = IO.readAccountsFromFile(MainActivity.myContext);

                if (!accountsStringFromFile.isEmpty()) {
                    List<Account> temp = IO.deserializeAccounts(accountsStringFromFile);

                    if (temp != null) {
                        accounts.addAll(temp);
                    }
                }

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (accounts.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onAccountsLoaded(accounts);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(readAccountsFromFile);
    }

    @Override
    public void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void saveAccount(@NonNull Account account, @NonNull SaveAccountCallback callback) {
        final List<Account> cachedAccounts = new ArrayList<>();

        cachedAccounts.addAll(AccountsRepository.getInstance().getCachedAccounts());

        /* add new account */
        cachedAccounts.add(account);

        Runnable writeAccountsToFile = new Runnable() {
            @Override
            public void run() {

                final List<Account> accounts = new ArrayList<>();

                String serializedAccount = IO.serializeAccounts(cachedAccounts);

                // success == 0, failure will return nonzero
                int failureCode = IO.writeAccountsToFile(serializedAccount, MainActivity.myContext);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (failureCode == 0) {
                            callback.onAccountSaved();
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(writeAccountsToFile);
    }

    @Override
    public void disableAccount(@NonNull String accountID) {

    }

    @Override
    public void deleteAllAccounts() {
        IO.deleteAccountsFile(MainActivity.myContext);
    }

    @Override
    public void refreshAccounts(@NonNull RefreshAccountsCallback callback) {

    }
}
