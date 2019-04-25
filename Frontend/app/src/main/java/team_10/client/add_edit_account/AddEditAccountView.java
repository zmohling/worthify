package team_10.client.add_edit_account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import team_10.client.R;

/**
 * Main UI for the add and edit account screen.
 */
public class AddEditAccountView extends Fragment implements AddEditAccountContract.View {

    private AddEditAccountContract.Presenter mPresenter;

    private LinearLayout mInputFieldList;

    private LinearLayout mTransactionItemList;

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

        Button saveButton = (Button) getActivity().findViewById(R.id.modal_add_edit_account_save);
        Button cancelButton = (Button) getActivity().findViewById(R.id.modal_add_edit_account_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveAccount();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
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
        mInputFieldList = (LinearLayout) root.findViewById(R.id.modal_add_edit_account_input_view_group);
        return root;
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void setAddConfirmButtonText() {

    }

    @Override
    public void insertAccountInputsView(View view) {

    }

    @Override
    public void insertTransactionsView(View view) {

    }
}
