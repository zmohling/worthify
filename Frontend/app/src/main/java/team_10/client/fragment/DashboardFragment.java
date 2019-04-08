package team_10.client.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import team_10.client.R;
import team_10.client.constant.TYPE;
import team_10.client.object.User;
import team_10.client.object.account.Account;
import team_10.client.settings.SharedPreferencesManager;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    public static CustomListAdapter customAdapter;
    public static ListView lv;

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
    // TODO: Rename and change types and number of parameters
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

        lv = view.findViewById(R.id.list);
        customAdapter = new CustomListAdapter(view.getContext(), R.layout.item_account_list_item, User.getAccounts());
        lv.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);

        System.out.println(IO.serializeAccounts(User.getAccounts()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
                startAddAccountModal();
                break;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
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

    public void startAddAccountModal() {
        final String label;
        final Account[] c = new Account[1];

        // inflate the layout of the popup window
        final LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        final View popupView = inflater.inflate(R.layout.modal_add_edit_account, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(100);
        }

        //final EditText editTextLabel = (EditText) popupView.findViewById(R.id.modal_account_label);
        Spinner spinner = (Spinner) popupView.findViewById(R.id.modal_account_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.account_types, R.layout.item_spinner_item);
        adapter.setDropDownViewResource(R.layout.item_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                TYPE t = TYPE.firstMatch(s);
                if (t != null) {
                    try {
                        c[0] = t.getTypeClass().newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }

                    ViewGroup insertPoint = (ViewGroup) popupView.findViewById(R.id.modal_add_edit_account_view_group);

                    // clear view
                    insertPoint.removeAllViews();
                    // insert into main view
                    insertPoint.addView(c[0].getView(getContext()), 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ((Button) popupView.findViewById(R.id.modal_account_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

//        ((Button) popupView.findViewById(R.id.modal_account_add)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random rand = new Random();
//                int i = rand.nextInt(4) + 1;
//
//                LocalDate now = LocalDate.now();
//
//                switch (c[0].getSimpleName()) {
//                    case "Loan":
//                        Loan l = new Loan();
//                        l.setLabel(editTextLabel.getText().toString());
//                        l.addTransaction(now = now.plusMonths(rand.nextInt(3) + 1),
//                                ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
//                                (rand.nextDouble() % 0.02 + 0.02), 1);
//                        User.addAccount(l);
//                        IO.sendAccountToRemote(l, getContext());
//                        break;
//
//                    case "SavingsAccount":
//                        SavingsAccount sA = new SavingsAccount();
//                        sA.setLabel(editTextLabel.getText().toString());
//                        sA.addTransaction(now = now.plusMonths(rand.nextInt(3) + 1),
//                                ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
//                                (rand.nextDouble() % 0.02 + 0.02), 0);
//                        User.addAccount(sA);
//                        IO.sendAccountToRemote(sA, getContext());
//                        break;
//
//                    case "CertificateOfDeposit":
//                        CertificateOfDeposit CoD = new CertificateOfDeposit();
//                        CoD.setLabel(editTextLabel.getText().toString());
//                        CoD.setMaturityDate(LocalDate.now().plusMonths(18));
//                        CoD.addTransaction(now = now.plusMonths(rand.nextInt(3) + 1),
//                                ((rand.nextDouble() * 700) * ((rand.nextInt() % 4 == 0) ? -1 : 1) + 300),
//                                (rand.nextDouble() % 0.02 + 0.02), 0);
//                        User.addAccount(CoD);
//                        IO.sendAccountToRemote(CoD, getContext());
//                        break;
//                }
//
//                customAdapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(lv);
//                popupWindow.dismiss();
//
//                //System.out.println(IO.serializeAccounts(MainActivity.user.getAccounts()));
//            }
//        });

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
