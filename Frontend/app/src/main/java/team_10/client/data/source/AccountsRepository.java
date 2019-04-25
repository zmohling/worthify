package team_10.client.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import team_10.client.activity.MainActivity;
import team_10.client.data.models.Account;
import team_10.client.data.source.local.AccountsLocalDataSource;
import team_10.client.data.source.remote.AccountsRemoteDataSource;
import team_10.client.utility.AppExecutors;

public class AccountsRepository implements AccountsDataSource {

    private static AccountsRepository INSTANCE = null;

    private final AccountsDataSource mAccountsRemoteDataSource;

    private final AccountsDataSource mAccountsLocalDataSource;

    Map<String, Account> mCachedAccounts;

    boolean mCacheIsDirty = false;

    // Private constructor for Singleton pattern
    private AccountsRepository(@NonNull AccountsDataSource accountsRemoteDataSource,
                               @NonNull AccountsDataSource accountsLocalDataSource) {
        mAccountsRemoteDataSource = accountsRemoteDataSource;
        mAccountsLocalDataSource = accountsLocalDataSource;
    }

    public static AccountsRepository getInstance() {
        if (INSTANCE == null) {
            AppExecutors appExecutors = new AppExecutors();

            AccountsLocalDataSource accountsLocalDataSource = AccountsLocalDataSource.getInstance(appExecutors);
            AccountsRemoteDataSource accountsRemoteDataSource = AccountsRemoteDataSource.getInstance(appExecutors);

            INSTANCE = new AccountsRepository(accountsRemoteDataSource, accountsLocalDataSource);
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getAccounts(@NonNull LoadAccountsCallback callback) {
        if (mCachedAccounts != null && mCachedAccounts.size() > 0 && !mCacheIsDirty) {
            callback.onAccountsLoaded(new ArrayList<>(mCachedAccounts.values()));
            return;
        }

        if (mCacheIsDirty) {
            getAccountsFromRemoteDataSource(callback);
        } else {
            mAccountsLocalDataSource.getAccounts(new LoadAccountsCallback() {
                @Override
                public void onAccountsLoaded(List<Account> accounts) {
                    refreshCache(accounts);
                    callback.onAccountsLoaded(new ArrayList<>(mCachedAccounts.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getAccountsFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void refreshCache(List<Account> accounts) {
        if (mCachedAccounts == null) {
            mCachedAccounts = new LinkedHashMap<>();
        }

        mCachedAccounts.clear();

        for (Account account : accounts) {
            mCachedAccounts.put(account.getID(), account);
        }

        mCacheIsDirty = false;
    }

    private void getAccountsFromRemoteDataSource(LoadAccountsCallback callback) {

    }

    @Override
    public void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback) {

        Account cachedAccount = getAccountWithID(accountID);

        if (cachedAccount != null) {
            callback.onAccountLoaded(cachedAccount);
            return;
        }

        mAccountsLocalDataSource.getAccount(accountID, new GetAccountCallback() {
            @Override
            public void onAccountLoaded(Account account) {
                if (mCachedAccounts == null) {
                    mCachedAccounts = new LinkedHashMap<>();
                }
                mCachedAccounts.put(account.getID(), account);
                callback.onAccountLoaded(account);
            }

            @Override
            public void onDataNotAvailable() {
                mAccountsRemoteDataSource.getAccount(accountID, new GetAccountCallback() {
                    @Override
                    public void onAccountLoaded(Account account) {
                        if (mCachedAccounts == null) {
                            mCachedAccounts = new LinkedHashMap<>();
                        }
                        mCachedAccounts.put(account.getID(), account);
                        callback.onAccountLoaded(account);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private Account getAccountWithID(String accountID) {
        if (mCachedAccounts.containsKey(accountID)) {
            return mCachedAccounts.get(accountID);
        } else {
            return null;
        }
    }

    @Override
    public void saveAccount(@NonNull Account account, @NonNull SaveAccountCallback callback) {
        if (mCachedAccounts == null) {
            mCachedAccounts = new LinkedHashMap<>();
        }
        mCachedAccounts.put(account.getID(), account);

        mAccountsLocalDataSource.saveAccount(account, callback);
        mAccountsRemoteDataSource.saveAccount(account, new SaveAccountCallback() {
            @Override
            public void onAccountSaved() {

            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void disableAccount(@NonNull String accountID) {
//        if (mCachedAccounts.containsKey(accountID)) {
//            mCachedAccounts.get(accountID);
//        }
    }

    @Override
    public void deleteAllAccounts() {
    }

    @Override
    public void refreshAccounts(@NonNull RefreshAccountsCallback callback) {

    }

    /**
     * Return a list of accounts that are currently stored in memory.
     * @return List of cached accounts
     */
    public List<Account> getCachedAccounts() {
        return new ArrayList<Account>(mCachedAccounts.values());
    }
}
