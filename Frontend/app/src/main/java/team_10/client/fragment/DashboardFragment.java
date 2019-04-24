package team_10.client.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.Random;

import team_10.client.R;
import team_10.client.object.User;
import team_10.client.data.models.Account;
import team_10.client.data.models.CertificateOfDeposit;
import team_10.client.data.models.Loan;
import team_10.client.data.models.SavingsAccount;
import team_10.client.settings.SharedPreferencesManager;
import team_10.client.utility.AccountModal;
import team_10.client.utility.CustomListAdapter;
import team_10.client.utility.IO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private static CustomListAdapter customAdapter;
    private static ListView lv;

    private OnFragmentInteractionListener mListener;


    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        customAdapter = new CustomListAdapter(view.getContext(), R.layout.item_account_list_item, User.getAccounts());
        lv.setAdapter(customAdapter);

        // Start Account Modal in edit mode if ListView item (account) was clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                startAccountModal(1, User.getAccounts().get(position));
            }
        });

        updateDashboardUI();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
                IO.deleteAccountsFile(parent.getApplicationContext());
                User.setAccounts(null);
                User.setToken(null);
                SharedPreferencesManager.getInstance(parent.getApplicationContext()).logout();
                parent.finish();
                break;
            case R.id.button_add_account:
                startAccountModal(0, null);
                break;
        }
    }

    /**
     * Start Account Modal
     *
     * @param mode 0 = Add Mode, 1 = Edit Mode
     * @param a    Account to edit, null otherwise
     */
    public void startAccountModal(int mode, Account a) {
        if (mode == 0) {
            new AccountModal(view).createAccountAddModal();
        } else {
            new AccountModal(view).createAccountEditModal(a);
        }
    }

    // Update ListView UI
    public static void updateDashboardUI() {
        customAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);
    }

    // Update ListView height equal to total height of child views
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

    // TODO: TEST METHOD; DELETE
    private void createRandomTransaction(Account a) {
        Random rand = new Random();
        int i = rand.nextInt(4) + 1;

        LocalDate now = (a.getTransactions().isEmpty()) ? LocalDate.now() : (LocalDate) a.getTransactions().keySet().toArray()[0];

        for (; i > 0; i--) {
            switch (a.getClass().getSimpleName()) {
                case "Loan":
                    ((Loan) a).addTransaction(now = now.minusMonths(rand.nextInt(3) + 1),
                            ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
                            (rand.nextDouble() % 0.02 + 0.02), 0);
                    break;

                case "SavingsAccount":
                    ((SavingsAccount) a).addTransaction(now = now.minusMonths(rand.nextInt(3) + 1),
                            ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
                            (rand.nextDouble() % 0.02 + 0.02), 0);
                    break;

                case "CertificateOfDeposit":
                    ((CertificateOfDeposit) a).addTransaction(now = now.minusMonths(rand.nextInt(3) + 1),
                            ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
                            (rand.nextDouble() % 0.02 + 0.02), 0);
                    break;
            }
        }
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
