package team_10.client.add_edit_account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import team_10.client.R;

public class AddEditAccountInputView extends Fragment implements AddEditAccountContract.InputView {

    private AddEditAccountContract.Presenter mPresenter;

    private TextView label;
    private EditText editText;

    public static AddEditAccountInputView newInstance() {
        return new AddEditAccountInputView();
    }

    public AddEditAccountInputView() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText = (EditText) getActivity().findViewById(R.id.item_string_input_view_INPUT);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_string_input_view, container, false);

        TextView label = (TextView) getActivity().findViewById(R.id.item_string_input_view_TITLE);

        return root;
    }

    @Override
    public void setPresenter(AddEditAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLabelText(String labelText) {
        label.setText(labelText);
    }

    @Override
    public void setEditLabelText(String editLabelText) {
        editText.setHint(editLabelText);
    }
}
