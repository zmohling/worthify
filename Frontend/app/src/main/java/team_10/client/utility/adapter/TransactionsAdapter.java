package team_10.client.utility.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.dashboard.add_edit_account.AddEditAccountPresenter;
import team_10.client.data.models.Transaction;

/**
 * Adapter class that keeps track of transaction for recycler view.
 */
public class TransactionsAdapter extends
        RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private Context parentContext;

    private final AddEditAccountPresenter mAddEditAccountPresenter;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTextView;
        public TextView descriptionTextView;

        public LinearLayout linearLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.contact_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.contact_description);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

    // Store a member variable for the contacts
    private List<Transaction> mArticles;

    // Pass in the contact array into the constructor
    public TransactionsAdapter(List<Transaction> articles, AddEditAccountPresenter addEditAccountPresenter) {
        this.mArticles = articles;
        this.mAddEditAccountPresenter = addEditAccountPresenter;
    }

    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        parentContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_transaction, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final TransactionsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Transaction article = mArticles.get(position);

        // Set item views based on your views and data model
        viewHolder.titleTextView.setText(article.getAccount().getLabel());
        viewHolder.descriptionTextView.setText(article.getValue() + "");

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddEditAccountPresenter.editTransaction(article.getDate());
            }
        });

        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(MainActivity.myContext)
                        .setTitle("Delete Transaction")
                        .setMessage("Are you sure you want to delete this transaction?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAddEditAccountPresenter.deleteTransaction(article.getDate());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
            }
        });

       /* viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(parentContext);
                View popupView = inflater.inflate(R.layout.modal_edit_transaction, null);

                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(100);
                }
                TextView nameText = (TextView) popupView.findViewById(R.id.modal_transaction_name);
                nameText.setText("Label: " + article.getAccount().getLabel());
                TextView textView = (TextView) popupView.findViewById(R.id.modal_transaction_value);
                textView.setText("Value of Transaction:  " + article.getValue());
                TextView dateText = (TextView) popupView.findViewById(R.id.modal_transaction_date);
                dateText.setText("Date of Transaction: " + article.getDate().toString());

                ((Button) popupView.findViewById(R.id.modal_transaction_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        */
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}