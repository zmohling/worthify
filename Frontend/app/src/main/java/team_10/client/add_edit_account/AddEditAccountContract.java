package team_10.client.add_edit_account;

import team_10.client.BasePresenter;
import team_10.client.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditAccountContract {

    /**
     * Presenter methods: Getter and setter methods must be included here when adding
     * new Account Types.
     */
    interface Presenter extends BasePresenter {

        // Interactions with View
        View getAccountInputsView();
        View getTransactionsView();
        void saveAccount();
        void cancel();
        void addTransaction();
        void deleteTransaction();

        boolean isDataMissing();
    }

    interface View extends BaseView<Presenter> {

        void setTitle();

        void setAddConfirmButtonText();

        void insertAccountInputsView(android.view.View view);

        void insertTransactionsView(android.view.View view);
    }
}
