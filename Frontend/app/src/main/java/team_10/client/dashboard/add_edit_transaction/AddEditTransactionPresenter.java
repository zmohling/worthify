package team_10.client.dashboard.add_edit_transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import team_10.client.data.models.Account;
import team_10.client.data.models.Transaction;
import team_10.client.data.source.AccountsDataSource;

public class AddEditTransactionPresenter implements AddEditTransactionContract.Presenter,
        AccountsDataSource.GetAccountCallback {

    private final AccountsDataSource mAccountsRepository;

    private final AddEditTransactionContract.View mAddEditTransactionView;

    private Transaction mTransactionModel;

    private String mAccountID;

    private boolean mIsDataMissing;

    public AddEditTransactionPresenter(@Nullable String accountID,
                                   @NonNull AccountsDataSource accountsRepository,
                                   @NonNull AddEditTransactionContract.View view,
                                   boolean shouldLoadDataFromRepo) {

        this.mAccountID = accountID;
        this.mAccountsRepository = accountsRepository;
        this.mAddEditTransactionView = view;
        this.mIsDataMissing = shouldLoadDataFromRepo;

        mAddEditTransactionView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void saveTransaction() {

    }

    @Override
    public void onAccountLoaded(Account account) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
