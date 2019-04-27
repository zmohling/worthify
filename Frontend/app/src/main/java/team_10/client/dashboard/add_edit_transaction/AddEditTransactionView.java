package team_10.client.dashboard.add_edit_transaction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.constant.PERIOD;

public class AddEditTransactionView extends Fragment implements AddEditTransactionContract.View {

    private AddEditTransactionContract.Presenter mPresenter;

    private LinearLayout mTransactionInputsView;

    private Spinner mRecurringSpinner;

    private TextView mTitle;

    private Button mSaveButton;

    private Button mCancelButton;

    public static AddEditTransactionView newInstance() {
        return new AddEditTransactionView();
    }

    public AddEditTransactionView() {
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
    public void setPresenter(@NonNull AddEditTransactionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSaveButton = (Button) getActivity().findViewById(R.id.modal_add_edit_transaction_save);
        mCancelButton = (Button) getActivity().findViewById(R.id.modal_add_edit_transaction_cancel);
        mRecurringSpinner = (Spinner) getActivity().findViewById(R.id.modal_add_edit_transaction_spinner);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTransaction();

                mPresenter.unsubscribe();

                getActivity().onBackPressed();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.unsubscribe();

                getActivity().onBackPressed();

            }
        });

        mRecurringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_transaction, container, false);

        mTitle = (TextView) root.findViewById(R.id.modal_add_edit_transaction_title);

        mTransactionInputsView = (LinearLayout) root.findViewById(R.id.modal_add_edit_transaction_input_view_group);

        /* Init spinner */
        List<String> periods = PERIOD.getAllAsStrings();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.myContext,
                android.R.layout.simple_spinner_item, periods);
        dataAdapter.setDropDownViewResource(R.layout.item_spinner_item);
        mRecurringSpinner.setAdapter(dataAdapter);

        return root;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setSaveButtonText(String text) {

    }

    @Override
    public void insertAccountInputsView(View view) {

    }

    @Override
    public boolean isActive() {
        return true;
    }
}
