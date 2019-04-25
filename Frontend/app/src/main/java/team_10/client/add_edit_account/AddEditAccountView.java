package team_10.client.add_edit_account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import team_10.client.R;

/**
 * Main UI for the add and edit account screen.
 */
public class AddEditAccountView extends Fragment implements AddEditAccountContract.View {

    private AddEditAccountContract.Presenter mPresenter;

    private LinearLayout mAccountInputsView;

    private LinearLayout mTransactionsView;

    private TextView mTitle;

    private Button mSaveButton;

    private Button mCancelButton;

    public static AddEditAccountView newInstance() {
        return new AddEditAccountView();
    }

    public AddEditAccountView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull AddEditAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSaveButton = (Button) getActivity().findViewById(R.id.modal_add_edit_account_save);
        mCancelButton = (Button) getActivity().findViewById(R.id.modal_add_edit_account_cancel);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveAccount();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_account, container, false);

        mTitle = (TextView) root.findViewById(R.id.modal_add_edit_account_title);

        // TODO: get linear layout for transactions

        return root;
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setAddConfirmButtonText(String text) {
        mSaveButton.setText(text);
    }

    @Override
    public void insertAccountInputsView(View view) {


    }

    @Override
    public void insertTransactionsView(View view) {

    }

    @Override
    public void showAccountEmptyError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
