package team_10.client.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.constant.PERIOD;
import team_10.client.constant.TYPE;
import team_10.client.dashboard.add_edit_account.AddEditAccountPresenter;
import team_10.client.dashboard.add_edit_account.AddEditAccountView;
import team_10.client.data.User;
import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.data.source.AccountsRepository;
import team_10.client.data.source.UserRepository;
import team_10.client.utility.adapter.AbstractAccountAdapter;
import team_10.client.utility.adapter.CustomListAdapter;

/**
 * A {@link Fragment} subclass that contains the graph and list of the user's accounts.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private View view;
    private static CustomListAdapter customAdapter;
    private static ListView lv;
    private static SwipeRefreshLayout pullToRefresh;
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

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(true);
            }
        });

        List<Entry> entries = new ArrayList<>();
        Random rand = new Random();
        int last = 0;
        for (int i = 0; i < 100; i++) {
            Entry e = new Entry();
            e.setX(i);
            if (last < 5) {
                last = last + Math.abs(rand.nextInt() % 6);
                e.setY(last);
            } else {
                last = last + rand.nextInt() % 6;
                e.setY(last);
            }
            entries.add(e);
        }

        //chart
        LineChart chart = view.findViewById(R.id.graph);
        LineDataSet dataset = new LineDataSet(entries, "sample");
        dataset.setDrawFilled(true);
        dataset.setDrawCircles(false);
        dataset.setFillAlpha(25);
        dataset.setHighlightEnabled(false);
        dataset.setColor(Color.parseColor("#16A085"));
        dataset.setFillColor(Color.parseColor("#16A085"));
        LineData lineData = new LineData(dataset);
        chart.setData(lineData);
        //chart.setAutoScaleMinMaxEnabled(true);
        chart.setDragYEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setMinOffset(0f);
        //chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.getAxisLeft().setTextSize(12);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLinesBehindData(true);
        chart.getAxisLeft().setTextColor(Color.parseColor("#80FFFFFF"));
        chart.getAxisLeft().setCenterAxisLabels(true);
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);


        // ListView of Accounts with adapter
        lv = view.findViewById(R.id.list);

        // Start Account Modal in edit mode if ListView item (account) was clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                startAddEditAccounts(accounts.get(position).getID(), TYPE.getType(accounts.get(position).getClass()));
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.myContext)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete this account?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAccountsRepository.disableAccount(accounts.get(position).getID());
                                updateDashboardUI();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
            }
        });

        refresh(false);

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

                if (User.getToken() != "") {
                    startAccountPickerPopup();
                    // TODO: dialog to warn user about adding accounts w/o internet connection
                } else {
                    Toast.makeText(MainActivity.myContext, "You must be a registered user to add an account.", Toast.LENGTH_SHORT).show();
                }
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

    /* Load AddEditAccounts view */
    public void startAddEditAccounts(@Nullable String accountID, @NonNull TYPE type) {
        Objects.requireNonNull(type);

        AddEditAccountView addEditAccountView = new AddEditAccountView();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, addEditAccountView, "")
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

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isActive() == 0) {
                accounts.remove(i);
            }
        }

        customAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);

    }

    /**
     * Load accounts from cache and getValues for graph
     * @param deepRefresh reload accounts from remote
     */
    public static void refresh(boolean deepRefresh) {

        if (deepRefresh)
            AccountsRepository.getInstance().refreshAccounts();

        AccountsRepository.getInstance().getAccounts(new AccountsDataSource.LoadAccountsCallback() {

            public void onAccountsLoaded(List<Account> accounts) {

                DashboardFragment.accounts = accounts;

                if (accounts == null)
                    DashboardFragment.accounts = new ArrayList<>();

                customAdapter = new CustomListAdapter(MainActivity.myContext, R.layout.item_account_list_item, accounts);
                lv.setAdapter(customAdapter);

                AccountsRepository.getInstance().getValues(PERIOD.DAY, new AccountsDataSource.GetValuesCallback() {
                    @Override
                    public void onValuesLoaded(Map<LocalDate, Double> values) {

                        GsonBuilder b = new GsonBuilder();
                        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
                        b.setPrettyPrinting();
                        Gson g = b.create();

                        System.out.println(g.toJson(values));

                        pullToRefresh.setRefreshing(false);

                        updateDashboardUI();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        pullToRefresh.setRefreshing(false);
                        updateDashboardUI();
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                pullToRefresh.setRefreshing(false);
                updateDashboardUI();
            }
        });
    }

    /**
     * Update ListView height equal to total height of child views
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
