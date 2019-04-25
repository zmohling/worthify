package team_10.client.add_edit_account;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;

public class AddEditAccountPresenter implements AddEditAccountContract.Presenter,
        AccountsDataSource.GetAccountCallback {

    private final AccountsDataSource mAccountsRepository;

    private final AddEditAccountContract.View mAddEditAccountView;

    /**
     * Accounts are immutable when not loaded into cache, so our view
     * will manipulate this dummy account, which will be either discarded
     * or added to the Accounts repository on "Add" or "Confirm" events
     */
    private Account mAccountModel;

    private String mAccountID;

    private boolean mIsDataMissing;

    //private final List<AddEditAccountContract.InputView> mInputViews;

    private boolean isDataMissing;

    public AddEditAccountPresenter(@Nullable String accountID, @NonNull AccountsDataSource accountsRepository,
                                   @NonNull AddEditAccountContract.View view, boolean shouldLoadDataFromRepo) {

        this.mAccountID = accountID;
        this.mAccountsRepository = accountsRepository;
        this.mAddEditAccountView = view;
        this.isDataMissing = shouldLoadDataFromRepo;
        //this.mInputViews = new ArrayList<>();

        mAddEditAccountView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public View getAccountInputsView() {
        return null;
    }

    @Override
    public View getTransactionsView() {
        return null;
    }

    @Override
    public void saveAccount() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void addTransaction() {

    }

    @Override
    public void deleteTransaction() {

    }

    @Override
    public boolean isDataMissing() {
        return false;
    }

    @Override
    public void onAccountLoaded(Account account) {

    }

    @Override
    public void onDataNotAvailable() {

    }

    interface CallBack {
        void onEvent(Object o);
    }
}
