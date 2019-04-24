package team_10.client.add_edit_account;

import team_10.client.data.models.Account;

public class AddEditAccountPresenter implements AddEditAccountContract.Presenter {

    private final Account mAccountModel;

    private final AddEditAccountContract.View mAddEditAccountView;

    //private final List<AddEditAccountContract.InputView> mInputViews;

    private boolean isDataMissing;

    public AddEditAccountPresenter(Account account, AddEditAccountContract.View view) {

        this.mAccountModel = account;
        this.mAddEditAccountView = view;
        //this.mInputViews = new ArrayList<>();

        isDataMissing = false;

        mAddEditAccountView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public AddEditAccountContract.View getAccountInputsView() {
        return null;
    }

    @Override
    public AddEditAccountContract.View getTransactionsView() {
        return null;
    }

    @Override
    public void saveAccount() {
        try {
            mAccountModel.getClass().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
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

    interface CallBack {
        void onEvent(Object o);
    }
}
