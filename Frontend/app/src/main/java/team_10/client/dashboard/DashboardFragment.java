package team_10.client.dashboard;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import java.util.List;
import java.util.Objects;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.constant.TYPE;
import team_10.client.dashboard.add_edit_account.AddEditAccountPresenter;
import team_10.client.dashboard.add_edit_account.AddEditAccountView;
import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.data.source.AccountsRepository;
import team_10.client.data.source.UserRepository;
import team_10.client.utility.adapter.CustomListAdapter;

/**
 * A {@link Fragment} subclass that contains the graph and list of the user's accounts.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {


    private View view;
    private static CustomListAdapter customAdapter;
    private static ListView lv;
    private static List<Account> accounts;

    private OnFragmentInteractionListener mListener;
    private AccountsRepository mAccountsRepository;
    private AddEditAccountPresenter mAddEditAccountPresenter;


    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountsRepository = AccountsRepository.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        view.findViewById(R.id.buttonLogout).setOnClickListener(this);
        view.findViewById(R.id.button_add_account).setOnClickListener(this);

        // ListView of Accounts with adapter
        lv = view.findViewById(R.id.list);

        // Start Account Modal in edit mode if ListView item (account) was clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                startAddEditAccounts(accounts.get(position).getID(), TYPE.getType(accounts.get(position).getClass()));
            }
        });

        updateDashboardUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Top level button listener
    @Override
    public void onClick(View v) {
        Activity parent = getActivity();

        switch (v.getId()) {
            case R.id.buttonLogout:
                UserRepository.getInstance().removeUserData();
                parent.finish();
                break;
            case R.id.button_add_account:

                startAccountPickerPopup();
                //startAddEditAccounts(null, Loan.class);

                break;
        }
    }

    /**
     * Number picker for choosing an account type to add
     */
    public void startAccountPickerPopup() {
        View popupView;
        final PopupWindow popupWindow;
        LayoutInflater inflater;

        inflater = LayoutInflater.from(MainActivity.myContext);
        popupView = inflater.inflate(R.layout.modal_wheel_picker, null);

        final List<String> accountTypes = TYPE.getAllAsStrings();

        final NumberPicker picker = popupView.findViewById(R.id.modal_account_picker);
        picker.setMinValue(0);
        picker.setMaxValue(accountTypes.size() - 1);
        picker.setDisplayedValues((accountTypes.toArray(new String[accountTypes.size()])));

        // Create a window for popupView
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);  // Ensure keyboard does not resize
        popupWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);

        Button continueButton = popupView.findViewById(R.id.modal_add_edit_account_continue);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE t = TYPE.getType(accountTypes.get(picker.getValue()));

                startAddEditAccounts(null, t);

                popupWindow.dismiss();
            }
        });

    }

    public void startAddEditAccounts(@Nullable String accountID, @NonNull TYPE type) {
        Objects.requireNonNull(type);

        AddEditAccountView addEditAccountView = new AddEditAccountView();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, addEditAccountView, "")
                .addToBackStack(null)
                .commit();


        mAddEditAccountPresenter = new AddEditAccountPresenter(accountID,
                type, mAccountsRepository, addEditAccountView,
                true);
    }

    /**
     * Update ListView UI
     */
    public static void updateDashboardUI() {

        AccountsRepository.getInstance().getAccounts(new AccountsDataSource.LoadAccountsCallback() {
            @Override
            public void onAccountsLoaded(List<Account> accounts) {
                customAdapter = new CustomListAdapter(MainActivity.myContext, R.layout.item_account_list_item, accounts);
                lv.setAdapter(customAdapter);

                DashboardFragment.accounts = accounts;

                customAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    /**
     *     Update ListView height equal to total height of child views
     */
    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + listView.getPaddingBottom();
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
