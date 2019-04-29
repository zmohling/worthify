package team_10.client.dashboard.add_edit_transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import team_10.client.MainActivity;
import team_10.client.dashboard.add_edit_account.AddEditAccountPresenter;
import team_10.client.data.UserInputField;
import team_10.client.data.models.Account;
import team_10.client.data.models.Transaction;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.utility.factory.InputFieldFactory;

public class AddEditTransactionPresenter implements AddEditTransactionContract.Presenter {

    private final AccountsDataSource mAccountsRepository;

    private final AddEditTransactionContract.View mAddEditTransactionView;

    private Transaction mTransactionModel;

    private Account mAccountModel;

    private LocalDate mTransactionID;

    private boolean mIsDataMissing;

    private AddEditAccountPresenter parent;

    public AddEditTransactionPresenter(@Nullable LocalDate transactionID, @NonNull Account account,
                                       @NonNull AccountsDataSource accountsRepository,
                                       @NonNull AddEditTransactionContract.View view,
                                       @Nullable AddEditAccountPresenter parentPresenter,
                                       boolean shouldLoadDataFromRepo) {

        this.mAccountModel = account;
        this.mTransactionID = transactionID;
        this.mAccountsRepository = accountsRepository;
        this.mAddEditTransactionView = view;
        this.mIsDataMissing = shouldLoadDataFromRepo;

        this.parent = parentPresenter;

        mAddEditTransactionView.setPresenter(this);
    }

    @Override
    public void subscribe() {

        if (isNewTransaction()) {

            mTransactionModel = mAccountModel.getTransaction(null);

            if (mAddEditTransactionView.isActive()) {

                mAddEditTransactionView.setTitle("Add Transaction");

                mAddEditTransactionView.setSaveButtonText("Add");

                mIsDataMissing = false;
            }

        } else {

            mTransactionModel = mAccountModel.getTransaction(mTransactionID);

            if (mAddEditTransactionView.isActive()) {

                mAddEditTransactionView.setTitle("Edit Transaction");

                mAddEditTransactionView.setSaveButtonText("Save");

                mIsDataMissing = false;

            }

        }

        generateAccountInputsView();
    }

    @Override
    public void unsubscribe() {
        parent.initializeTransactionViews();
    }

    @Override
    public void saveTransaction() {
        mAccountModel.addTransaction(mTransactionModel);
    }

    @Override
    public void setRecurring(int recurring) {
        mTransactionModel.setRecurring(recurring);
    }

    private boolean isNewTransaction() {
        return mTransactionID == null;
    }


    private Map<Field, UserInputField> fields;

    private void generateAccountInputsView() {

        LinearLayout v = new LinearLayout(MainActivity.myContext);

        v.setOrientation(LinearLayout.VERTICAL);

        fields = new TreeMap<>((o1, o2) -> o1.getAnnotation(UserInputField.class).priority() -
                o2.getAnnotation(UserInputField.class).priority());

        for (Field field : mTransactionModel.getClass().getFields()) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(UserInputField.class)) {

                fields.put(field, field.getAnnotation(UserInputField.class));

            }
        }

        for (Field field : fields.keySet()) {
            v.addView(InputFieldFactory
                    .getInputFieldView(mTransactionModel, field)
            );
        }

        mAddEditTransactionView.insertAccountInputsView(v);
    }

    @Override
    public boolean isDataMissing() {
        mIsDataMissing = false;

        for (Field field : fields.keySet()) {
            try {
                if (field.get(mTransactionModel) == null) {

                    mAddEditTransactionView.showNullFieldError();

                    mIsDataMissing = true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return mIsDataMissing;
    }
}
