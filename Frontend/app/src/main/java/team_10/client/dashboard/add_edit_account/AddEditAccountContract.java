package team_10.client.dashboard.add_edit_account;

import team_10.client.BasePresenter;
import team_10.client.BaseView;
import team_10.client.dashboard.add_edit_transaction.AddEditTransactionContract;
import team_10.client.utility.adapter.TransactionsAdapter;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditAccountContract {

    /**
     * Presenter methods: Getter and setter methods must be included here when adding
     * new Account Types.
     */
    interface Presenter extends BasePresenter {

        void saveAccount();

        void cancel();

        void addTransaction();

        void editTransaction();

        void deleteTransaction();

        boolean isDataMissing();
    }

    interface View extends BaseView<Presenter> {

        void setTitle(String title);

        void setSaveButtonText(String text);

        void insertAccountInputsView(android.view.View view);

        void setTransactionRecyclerAdapter(TransactionsAdapter adapter);

        void setRecurringTransactionRecyclerAdapter(TransactionsAdapter adapter);

        void showNullFieldError();

        AddEditTransactionContract.View showAddEditTransactionView();

        void notifyDatasetChanged();

        boolean isActive();
    }
}
