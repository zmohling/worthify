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
        void populateAccountandTransactionLists();

        void saveAccount();

        // Interactions with InputFieldView
        void populateInputView();

        // Interactions with TransactionView
        void populateTransactionView();


        boolean isDataMissing();
    }

    interface View extends BaseView<Presenter> {

        void addInputField(android.view.View view);

        void addTransactionItem(android.view.View view);

    }

    /**
     * The subview contains the dynamically generated input views.
     */
    interface InputView extends BaseView<Presenter>{

        void setLabelText(String labelText);

        void setEditLabelText(String editLabelText);

    }

    interface TransactionView extends BaseView<Presenter>{

    }

}
