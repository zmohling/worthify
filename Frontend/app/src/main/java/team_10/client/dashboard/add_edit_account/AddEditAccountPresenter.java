package team_10.client.dashboard.add_edit_account;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import team_10.client.MainActivity;
import team_10.client.constant.TYPE;
import team_10.client.dashboard.DashboardFragment;
import team_10.client.data.UserInputField;
import team_10.client.data.models.Account;
import team_10.client.data.models.Transaction;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.utility.adapter.TransactionsAdapter;
import team_10.client.utility.factory.InputFieldFactory;

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

    private TYPE mType;

    private String mAccountID;

    private boolean mIsDataMissing;

    public AddEditAccountPresenter(@Nullable String accountID, @NonNull TYPE type,
                                   @NonNull AccountsDataSource accountsRepository,
                                   @NonNull AddEditAccountContract.View view,
                                   boolean shouldLoadDataFromRepo) {

        this.mAccountID = accountID;
        this.mType = type;
        this.mAccountsRepository = accountsRepository;
        this.mAddEditAccountView = view;
        this.mIsDataMissing = shouldLoadDataFromRepo;

        mAddEditAccountView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (isNewAccount()) {

            mAccountsRepository.newAccount(mType, this);

            if (mAddEditAccountView.isActive()) {

                mAddEditAccountView.setTitle("Add: " + mType.toString());

                mAddEditAccountView.setAddConfirmButtonText("Add");

            }

        } else {

            mAccountsRepository.getAccountCopy(mAccountID, this);

            if (mAddEditAccountView.isActive()) {

                mAddEditAccountView.setTitle("Edit: " + mType.toString());

                mAddEditAccountView.setAddConfirmButtonText("Save");

            }

        }
    }

    @Override
    public void unsubscribe() {
        DashboardFragment.updateDashboardUI();
    }


    @Override
    public void saveAccount() {

        mAccountsRepository.saveAccount(mAccountModel, new AccountsDataSource.SaveAccountCallback() {
            @Override
            public void onAccountSaved() {
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

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
        return mIsDataMissing;
    }

    @Override
    public void onAccountLoaded(Account account) {
        mAccountModel = account;
        mType = TYPE.getType(account.getClass());
        mAccountID = account.getID();

        if (mAddEditAccountView.isActive()) {

            generateAccountInputsView();

            initializeTransactionViews();

        }

        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        mAddEditAccountView.showAccountEmptyError();
    }

    private boolean isNewAccount() {
        return mAccountID == null;
    }

    private void generateAccountInputsView() {

        Map<Field, UserInputField> fields;

        LinearLayout v = new LinearLayout(MainActivity.myContext);

        v.setOrientation(LinearLayout.VERTICAL);

        fields = new TreeMap<>((o1, o2) -> o1.getAnnotation(UserInputField.class).priority() -
                o2.getAnnotation(UserInputField.class).priority());

        for (Field field : mType.getClazz().getFields()) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(UserInputField.class)) {

                fields.put(field, field.getAnnotation(UserInputField.class));

            }
        }

        for (Field field : fields.keySet()) {
            v.addView(InputFieldFactory
                    .getInputFieldView(mAccountModel, field)
            );
        }

        mAddEditAccountView.insertAccountInputsView(v);
    }

    private void initializeTransactionViews() {
        TransactionsAdapter adapter = new TransactionsAdapter(
                getTransactions(mAccountModel)
        );

        TransactionsAdapter recurringAdapter = new TransactionsAdapter(
                getRecurringTransactions(mAccountModel)
        );

        mAddEditAccountView.setTransactionRecyclerAdapter(adapter);

        mAddEditAccountView.setRecurringTransactionRecyclerAdapter(recurringAdapter);

    }

    private List<Transaction> getTransactions(Account account) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            transactions.addAll(account.getTransactions().values());
        } catch (NullPointerException e) {
        }

        return transactions;
    }

    private List<Transaction> getRecurringTransactions(Account account) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            transactions.addAll(account.getTransactions().values());
        } catch (NullPointerException e) {
        }

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getRecurring() == 0) {
                transactions.remove(i);
            }
        }
        return transactions;
    }
}

