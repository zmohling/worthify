package team_10.client.object.account;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Vector;

import team_10.client.utility.VolleySingleton;

import static team_10.client.constant.URL.ROOT_URL;

/**
 * Real Estate account type.
 */
public class RealEstate extends Account {
    public String address;
    public String city;
    public String state;
    public Context context;

    public RealEstate(Context context, String address, String city, String state) {
        this.context = context;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    /**
     * Overloaded add transaction to account.
     * @param d date
     * @param value value
     */
    public void addTransaction(LocalDate d, double value)
    {
        addTransaction(d, value, 0, transactions.size());
    }

    public void addTransaction(LocalDate d, double value, int recurring, int transactionId)
    {
        Transaction t = new Transaction(value, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    /**
     * Overloaded getValue method from account.
     * @param d date
     * @return double value
     */
    public double getValue(LocalDate d) {
        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());

        double total = 0;
        final double[] stockValue = new double[1];

        if (transaction_dates.size() <= 0) {
            throw new IllegalStateException("No transactions for this account.");
        } else {
            for (int i = 0; i < transaction_dates.size(); i++) {

                LocalDate fromDate = transaction_dates.get(i);
                LocalDate toDate;

                if ((i + 1) >= transaction_dates.size() || transaction_dates.get(i + 1).isAfter(d)) {
                    toDate = d;
                    i = Integer.MAX_VALUE - 1; // Stop calculating if date d is before last transaction
                } else {
                    toDate = transaction_dates.get(i + 1);
                }
                String urlStock = ROOT_URL + address + city + state;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStock,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject returned = new JSONObject(response);
                                    try {
                                        //JSONObject userJson = returned.getJSONObject("value");
                                        stockValue[0] = returned.getDouble("value");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.getMessage());
                            }
                        });
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        }

        return stockValue[0]; // round to nearest cent
    }

    @Override
    public View getView(Context context) {
        return null;
    }

    private class Transaction extends team_10.client.object.account.Transaction {

        Transaction(double value, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }
    }
}
