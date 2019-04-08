package team_10.client.object.account;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import team_10.client.constant.URL;
import team_10.client.object.User;
import team_10.client.utility.VolleySingleton;

public class Stock extends Account {
    public String ticker;
    public int amount;
    public double valueOfStock;


    public Stock() {
    }

    public Stock(Context context){
        super(context);
    }

    public String getTicker() { return ticker; }
    public void setTicker(String symbol) { this.ticker = symbol; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

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
        valueOfStock = 0;

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
                    JsonArray accountIdArray = new JsonArray();
                    accountIdArray.add(accountID);
                    userAccountsRequest.add("accountID", accountIdArray);
                    requestBody = userAccountsRequest.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String finalRequestBody = requestBody;
                /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_GET_API,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject returned = new JSONObject(response);

                                    if (returned.has(accountID)) {
                                        valueOfStock = returned.getDouble(accountID);
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


                };*/

                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                //requestQueue.add(stringRequest);
                //requestQueue.start();

                RequestFuture<String> future = RequestFuture.newFuture();
                JSONObject userAccountsRequest = new JSONObject();
                JSONArray accountIdArray = new JSONArray();
                accountIdArray.put(accountID);
                try {
                    userAccountsRequest.put("accountID", accountIdArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringRequest request = new StringRequest(Request.Method.POST, URL.URL_GET_API, future, future){
                    @Override
                    public String getBodyContentType() {
                        return String.format("text/plain;charset=UTF-8");
                    }

                    @Override
                    public byte[] getBody() {
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
                        Map<String, String> params = new HashMap<String, String>();
                        final String basicAuth = User.getToken();
                        params.put("Authorization", basicAuth);

                        return params;
                    }
                };
                requestQueue.add(request);



                try {
                    String returned = future.get(10000, TimeUnit.MILLISECONDS); // this will block
                    try {
                        JSONObject response = new JSONObject(returned);
                        if (response.has(accountID)) {
                            valueOfStock = response.getDouble(accountID);
                            System.out.println("Accounts Retrieval Successful");
                        }  else {
                            System.out.println("Accounts Retrieval Unsuccessful");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // gson catch exceptions
                    }
                } catch (InterruptedException e) {
                    // exception handling
                } catch (ExecutionException e) {
                    // exception handling
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                //waitForResponse(); */
            }
        }
        return valueOfStock; // round to nearest cent
    }

    private boolean waitForResponse()
    {
        for (int i = 0; i < 10; i++)
        {
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (valueOfStock != 0)
            {
                return true;
            }
        }
        return false;
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
