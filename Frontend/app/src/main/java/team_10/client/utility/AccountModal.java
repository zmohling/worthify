package team_10.client.utility;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team_10.client.R;
import team_10.client.constant.TYPE;
import team_10.client.fragment.DashboardFragment;
import team_10.client.object.User;
import team_10.client.data.models.Account;
import team_10.client.data.models.Transaction;

public class AccountModal {
    private Context context;
    private View parent, popupView;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private ViewGroup insertPoint;

    private Account a_temp = null; // temp variable for dumb Java inner class access rules

    public AccountModal(View parent) {
        this.parent = parent;
        this.context = parent.getContext();

        initPopup();
    }

    private void initPopup() {
        // inflate the layout of the popup window
        inflater = LayoutInflater.from(context);
        popupView = inflater.inflate(R.layout.modal_add_edit_account, null);

        // Create a window for popupView
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);  // Ensure keyboard does not resize
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        // Insert point for dynamic accou
        insertPoint = (ViewGroup) popupView.findViewById(R.id.modal_add_edit_account_view_group);


        // Cancel button
        ((Button) popupView.findViewById(R.id.modal_account_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(100);
        }
    }

    /**
     * Creates a PopupWindow view where user adds an Account
     */
    public void createAccountAddModal() {
        // Change Title text
        TextView textView = popupView.findViewById(R.id.modal_account_text);
        textView.setText("Add an Account");

        // Change Button text
        Button confirmButton = popupView.findViewById(R.id.modal_account_add);
        confirmButton.setText("Add");

        Spinner spinner = (Spinner) popupView.findViewById(R.id.modal_account_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context.getApplicationContext(), R.array.account_types, R.layout.item_spinner_item);
        adapter.setDropDownViewResource(R.layout.item_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                TYPE t = TYPE.firstMatch(s);
                if (t != null) {
                    try {
                        a_temp = t.getTypeClass().newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }



                    // clear view
                    insertPoint.removeAllViews();
                    // insert into main view
                    insertPoint.addView(InputFieldFactory.getInputFieldView(a_temp, context), 0);
                    // insert into main view
                    final View transactionView = createTransactionView(a_temp, inflater);
                    insertPoint.addView(transactionView);

                    ((Button) insertPoint.findViewById(R.id.button_add_transaction)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //createRandomTransaction(a);
                            insertPoint.removeView(transactionView);
                            insertPoint.addView(createTransactionView(a_temp, inflater));
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add button
        ((Button) popupView.findViewById(R.id.modal_account_add)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                User.addAccount(a_temp);
                IO.sendAccountToRemote(a_temp, context);

                DashboardFragment.updateDashboardUI();
                popupWindow.dismiss();

                System.out.println(IO.serializeAccounts(User.getAccounts()));
            }
        });
    }

    /**
     * Creates a PopupWindow view where the user edits an account.
     * @param a Account to edit
     */
    public void createAccountEditModal(final Account a) {
        a_temp = a;

        // Change Title text
        TextView textView = popupView.findViewById(R.id.modal_account_text);
        textView.setText("Edit Account");

        // Change Button text
        Button confirmButton = popupView.findViewById(R.id.modal_account_add);
        confirmButton.setText("Confirm");

        Spinner spinner = (Spinner) popupView.findViewById(R.id.modal_account_spinner);
        spinner.setVisibility(View.GONE);

        try {
            ArrayList<Account> aListTemp = new ArrayList<>();
            aListTemp.add(a);

            a_temp = (Account) IO.deserializeAccounts(IO.serializeAccounts(aListTemp)).get(0);

            // clear view
            insertPoint.removeAllViews();
            // insert into main view
            insertPoint.addView(a_temp.getView(context), 0);
            // insert into main view
            final View transactionView = createTransactionView(a_temp, inflater);
            insertPoint.addView(transactionView);

            ((Button) insertPoint.findViewById(R.id.button_add_transaction)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //createRandomTransaction(a_temp);
                    insertPoint.removeView(transactionView);
                    insertPoint.addView(createTransactionView(a_temp, inflater));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Confirm button
        ((Button) popupView.findViewById(R.id.modal_account_add)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Remove existing account and replace with our modified copy
                User.removeAccount(a);
                User.addAccount(a_temp);

                DashboardFragment.updateDashboardUI();
                popupWindow.dismiss();

                System.out.println(IO.serializeAccounts(User.getAccounts()));
            }
        });
    }

    private View createTransactionView(Account a, LayoutInflater inflater) {
        ArrayList<Transaction> transactions;
        ArrayList<Transaction> recurringTransactions;
        TransactionsAdapter adapter;
        TransactionsAdapter recurringAdapter;
        RecyclerView recyclerView;
        RecyclerView recurringRecyclerView;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.modal_transactions, null, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvTransactions);

        transactions = new ArrayList<Transaction>();
        adapter = new TransactionsAdapter(transactions);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recurringRecyclerView = (RecyclerView) view.findViewById(R.id.rvRecurringTransactions);

        recurringTransactions = new ArrayList<Transaction>();
        recurringAdapter = new TransactionsAdapter(recurringTransactions);
        recurringRecyclerView.setAdapter(recurringAdapter);
        recurringRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recurringRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        transactions.addAll(getTransactions(a));
        //recurringTransactions.addAll(getRecurringTransactions(a));

        return view;
    }

    private List<Transaction> getTransactions(Account a) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            transactions.addAll(a.getTransactions().values());
        } catch (NullPointerException e) {
        }

        return transactions;
    }

    private List<Transaction> getRecurringTransactions(Account a) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            transactions.addAll(a.getTransactions().values());
        } catch (NullPointerException e) {
        }

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getRecurring() == 0) {
                transactions.remove(i);
            }
        }
        return transactions;
    }
}
