package team_10.client.data.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import team_10.client.MainActivity;
import team_10.client.constant.PERIOD;
import team_10.client.constant.TYPE;
import team_10.client.data.models.Account;
import team_10.client.data.source.local.AccountsLocalDataSource;
import team_10.client.data.source.remote.AccountsRemoteDataSource;
import team_10.client.utility.General;
import team_10.client.utility.adapter.AbstractAccountAdapter;
import team_10.client.utility.io.AppExecutors;
import team_10.client.utility.io.IO;

public class AccountsRepository implements AccountsDataSource {

    private static AccountsRepository INSTANCE = null;

    private final AccountsDataSource mAccountsRemoteDataSource;

    private final AccountsDataSource mAccountsLocalDataSource;

    private final UserDataSource mUserRepository;

    Map<String, Account> mCachedAccounts;

    boolean mCacheIsDirty = false;

    // Binary value for if account data has been queued
    // to send to the server
    boolean mAccountDataQueued = false;

    // Private constructor for Singleton pattern
    private AccountsRepository(@NonNull AccountsDataSource accountsRemoteDataSource,
                               @NonNull AccountsDataSource accountsLocalDataSource,
                               @NonNull UserDataSource userDataSource) {
        mAccountsRemoteDataSource = accountsRemoteDataSource;
        mAccountsLocalDataSource = accountsLocalDataSource;
        mUserRepository = userDataSource;

        mCachedAccounts = new LinkedHashMap<>();

        // TODO: Fetch remote accounts on every boot
    }

    public static AccountsRepository getInstance() {
        if (INSTANCE == null) {
            AppExecutors appExecutors = MainActivity.mAppExecutors;

            AccountsLocalDataSource accountsLocalDataSource = AccountsLocalDataSource.getInstance(appExecutors);
            AccountsRemoteDataSource accountsRemoteDataSource = AccountsRemoteDataSource.getInstance(appExecutors);
            UserDataSource userDataSource = UserRepository.getInstance();

            INSTANCE = new AccountsRepository(accountsRemoteDataSource, accountsLocalDataSource, userDataSource);
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

    private void getAccountsFromRemoteDataSource(@NonNull final LoadAccountsCallback callback) {
        mAccountsRemoteDataSource.getAccounts(new LoadAccountsCallback() {
            @Override
            public void onAccountsLoaded(List<Account> accounts) {
                refreshCache(accounts);
                refreshLocalDataSource(accounts);
                callback.onAccountsLoaded(accounts);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
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

    private void refreshLocalDataSource(List<Account> accounts) {
        mAccountsLocalDataSource.deleteAllAccounts();

        if (accounts.isEmpty()) {
            return;
        }

        /* AccountsLocalDataSource.saveAccount first loads cached accounts, so   *
         * it will work more efficiently to do this one instead of every account */
        mAccountsLocalDataSource.saveAccount(accounts.get(0), new SaveAccountCallback() {
            @Override
            public void onAccountSaved() {
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

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

    @Override
    public void newAccount(@NonNull TYPE type, @NonNull GetAccountCallback callback) {

        Account account = null;

        UserDataSource userRepository = UserRepository.getInstance();

        int numAccounts = userRepository.getNumAccountsCreated();
        for (Account cachedAccount : mCachedAccounts.values()) {
            int accountID = Integer.parseInt(cachedAccount.getID().substring(8));
            if (accountID > numAccounts)
                numAccounts = accountID;
        }
        mUserRepository.setNumAccountsCreated(numAccounts);

        try {
            account = type.getClazz().newInstance();

            if (account != null) {

                int userID = userRepository.getUserID();
                int num = userRepository.getNumAccountsCreated() + 1;
                String accountID = String.format("%08d", userID) + String.format("%04d", (num++));

                account.setID(accountID);

                account.setIsActive(1);

                callback.onAccountLoaded(account);
            } else {
                callback.onDataNotAvailable();
            }

        } catch (IllegalAccessException e) {
            callback.onDataNotAvailable();
            e.printStackTrace();
        } catch (InstantiationException e) {
            callback.onDataNotAvailable();
            e.printStackTrace();
        }
    }

    @Override
    public void getAccountCopy(@NonNull String accountID, @NonNull GetAccountCallback callback) {
        getAccount(accountID, new GetAccountCallback() {
            @Override
            public void onAccountLoaded(Account account) {

                List<Account> accountWrapperCollection = new ArrayList<Account>();

                accountWrapperCollection.add(account);

                Account deepCopyAccount = null;

                if (!accountWrapperCollection.isEmpty()) {

                    deepCopyAccount = IO.deserializeAccounts(
                            IO.serializeAccounts(accountWrapperCollection)).get(0);

                    if (deepCopyAccount != null)
                        callback.onAccountLoaded(deepCopyAccount);
                    else
                        callback.onDataNotAvailable();

                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
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

        /* Cheap implementation to know if account is new or not for remote */
        if (mCachedAccounts.containsKey(account.getID())) {

            mCachedAccounts.put(account.getID(), account);

            mAccountsLocalDataSource.saveAccount(account, callback);

            sendAccountToRemoteDataSource(account, callback);

        } else {


            sendAccountToRemoteDataSource(account, callback);

            mCachedAccounts.put(account.getID(), account);

            mAccountsLocalDataSource.saveAccount(account, callback);
        }

    }

    private void sendAccountToRemoteDataSource(Account account, SaveAccountCallback callback) {
        mAccountsRemoteDataSource.saveAccount(account, new SaveAccountCallback() {
            @Override
            public void onAccountSaved() {

            }

            @Override
            public void onDataNotAvailable() {

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
        mCacheIsDirty = true;
        mCachedAccounts.clear();
        mAccountsLocalDataSource.deleteAllAccounts();
    }

    @Override
    public void refreshAccounts(@NonNull LoadAccountsCallback callback) {

    }

    @Override
    public Map<LocalDate, Double> getValues(@NonNull PERIOD period) {

        Map<String, Map<LocalDate, Double>> wrapper = new HashMap<>();
        Map<LocalDate, Double> dailyTotalValues = new HashMap<>();

        dailyTotalValues.put(LocalDate.now().minusDays(4), General.round(100.055, 2));
        dailyTotalValues.put(LocalDate.now().minusDays(3), General.round(105.22255, 2));
        dailyTotalValues.put(LocalDate.now().minusDays(2), General.round(120.314, 2));
        dailyTotalValues.put(LocalDate.now(), General.round(150.01, 2));

        wrapper.put("000006820006", dailyTotalValues);

        Map<LocalDate, Double> dailyTotalValues1 = new HashMap<>();

        dailyTotalValues1.put(LocalDate.now().minusDays(3), General.round(9505.6666, 2));
        dailyTotalValues1.put(LocalDate.now().minusDays(2), General.round(10022, 2));
        dailyTotalValues1.put(LocalDate.now().minusDays(1), General.round(11000.99, 2));
        dailyTotalValues1.put(LocalDate.now(), General.round(11000.99, 2));

        wrapper.put("000006820007", dailyTotalValues1);

        Map<LocalDate, Double> dailyTotalValues2 = new HashMap<>();

        dailyTotalValues2.put(LocalDate.now(), General.round(21.2001, 2));

        wrapper.put("000006820008", dailyTotalValues2);

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        b.setPrettyPrinting();
        Gson g = b.create();

        System.out.println(g.toJson(wrapper));

        return  dailyTotalValues;

    }

    /**
     * Return a list of accounts that are currently stored in memory.
     *
     * @return List of cached accounts
     */
    public Map<String, Account> getCachedAccounts() {
        return mCachedAccounts;
    }
}

