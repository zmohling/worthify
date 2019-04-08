package team_10.client.object.account;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import team_10.client.constant.URL;
import team_10.client.fragment.DashboardFragment;
import team_10.client.object.User;
import team_10.client.settings.SharedPreferencesManager;
import team_10.client.utility.VolleySingleton;

import static team_10.client.constant.URL.ROOT_URL;

public class Stock extends Account {
    public String symbol;
    public Context context;
    public int amount;


    public Stock(Context context, String symbol) {
        this.symbol = symbol;
        this.context = context;
    }

    public void addTransaction(LocalDate d, double value, int amount)
    {
        addTransaction(d, amount, 0, value, transactions.size());
    }

    public void addTransaction(LocalDate d, int amount, int recurring, double value, int transactionId) {
        this.amount = this.amount + amount;
        if (this.amount < 0)
        {
            this.amount = 0;
        }
        Transaction t = new Transaction(value, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

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
                /*
                String urlStock = ROOT_URL + symbol;
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
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);*/

                String requestBody = null;
                try {
                    JsonObject userAccountsRequest = new JsonObject();
                    JsonArray accountId = new JsonArray();
                    accountId.add(this.accountID);
                    userAccountsRequest.add("accountID", accountId);
                    requestBody = userAccountsRequest.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String finalRequestBody = requestBody;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_GET_API,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject returned = new JSONObject(response);

                                    if (returned.has(accountID)) {
                                        stockValue[0] = returned.getDouble(accountID);

                                        System.out.println("Accounts Retrieval Successful");
                                    }  else {
                                        System.out.println("Accounts Retrieval Unsuccessful");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // gson catch exceptions
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,
                                        (error.getMessage() == null) ? "Error Syncing with Server" : "Error Syncing with Server: " + error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                System.out.println(error.toString());
                                error.printStackTrace();
                            }
                        }) {

                    @Override
                    public String getBodyContentType() {
                        return String.format("text/plain;charset=UTF-8");
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return finalRequestBody == null ? null : finalRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    finalRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        final String basicAuth = User.getToken();
                        params.put("Authorization", basicAuth);

                        return params;
                    }


                };

                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        }

        return stockValue[0] * amount; // round to nearest cent
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
