package team_10.client.dashboard.add_edit_account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import team_10.client.R;
import team_10.client.utility.adapter.TransactionsAdapter;

/**
 * Main UI for the add and edit account screen.
 */
public class AddEditAccountView extends Fragment implements AddEditAccountContract.View {

    private AddEditAccountContract.Presenter mPresenter;

    private LinearLayout mAccountInputsView;

    private RecyclerView mRecurringTransactionsView;

    private RecyclerView mTransactionsView;

    private TextView mTitle;

    private Button mAddTransactionButton;

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
        mAddTransactionButton = (Button) getActivity().findViewById(R.id.modal_add_edit_account_add_transaction);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveAccount();

                mPresenter.unsubscribe();

                getActivity().onBackPressed();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.unsubscribe();

                getActivity().onBackPressed();

                System.out.println("TOUCH");
            }
        });

        mAddTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_account, container, false);

        mTitle = (TextView) root.findViewById(R.id.modal_add_edit_account_title);

        mAccountInputsView = (LinearLayout) root.findViewById(R.id.modal_add_edit_account_input_view_group);

        mTransactionsView = (RecyclerView) root.findViewById(R.id.modal_add_edit_account_transaction_view_group);
        mTransactionsView.setItemAnimator(new DefaultItemAnimator());
        mTransactionsView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecurringTransactionsView = (RecyclerView) root.findViewById(
                R.id.modal_add_edit_account_recurring_transaction_view_group);
        mRecurringTransactionsView.setItemAnimator(new DefaultItemAnimator());
        mRecurringTransactionsView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setSaveButtonText(String text) {
        mSaveButton.setText(text);
    }

    @Override
    public void insertAccountInputsView(View view) {
        mAccountInputsView.addView(view, 0);
    }

    @Override
    public void showAccountEmptyError() {

    }

    @Override
    public void setTransactionRecyclerAdapter(TransactionsAdapter adapter) {
        mTransactionsView.setAdapter(adapter);
    }

    @Override
    public void setRecurringTransactionRecyclerAdapter(TransactionsAdapter adapter) {
        mRecurringTransactionsView.setAdapter(adapter);
    }

    @Override
    public void notifyDatasetChanged() {
        if (mTransactionsView.getAdapter() == null ||
                mRecurringTransactionsView.getAdapter() == null) {
        } else {
            mTransactionsView.getAdapter().notifyDataSetChanged();
            mRecurringTransactionsView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
