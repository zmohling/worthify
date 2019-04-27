package team_10.client.dashboard.add_edit_transaction;

import team_10.client.BasePresenter;
import team_10.client.BaseView;

public interface AddEditTransactionContract {

    interface Presenter extends BasePresenter {

        void saveTransaction();


    }

    interface View extends BaseView<Presenter> {

        void setTitle(String title);

        void setSaveButtonText(String text);

        void insertAccountInputsView(android.view.View view);

        boolean isActive();

    }
}
