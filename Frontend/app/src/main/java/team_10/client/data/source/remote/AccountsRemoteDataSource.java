package team_10.client.data.source.remote;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team_10.client.MainActivity;
import team_10.client.constant.URL;
import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.dashboard.DashboardFragment;
import team_10.client.data.User;
import team_10.client.data.source.local.SharedPreferencesManager;
import team_10.client.utility.io.AppExecutors;
import team_10.client.utility.io.IO;
import team_10.client.utility.io.VolleySingleton;

public class AccountsRemoteDataSource implements AccountsDataSource {

    private static volatile AccountsRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private AccountsRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static AccountsRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (AccountsRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AccountsRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAccounts(@NonNull LoadAccountsCallback callback) {
        List<Account> returnVal = new ArrayList<>();
        String requestBody = null;
        try {
            JsonObject userAccountsRequest = new JsonObject();
            userAccountsRequest.addProperty("id", Integer.toString(SharedPreferencesManager.getUser().getID()));
            requestBody = userAccountsRequest.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalRequestBody = requestBody;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_GET_ACCOUNTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.contains("\"accounts\":")) {
                                List<Account> l = IO.deserializeAccounts(response);

                                System.out.println("Fetched from server:\n" + IO.serializeAccounts(l));

                                callback.onAccountsLoaded(l);

                                DashboardFragment.updateDashboardUI();

                                System.out.println("Accounts Retrieval Successful");
                            } else {
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
                        Toast.makeText(MainActivity.myContext,
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
                Map<String, String> params = new HashMap<String, String>();
                final String basicAuth = User.getToken();
                params.put("Authorization", basicAuth);

                return params;
            }


        };

        VolleySingleton.getInstance(MainActivity.myContext).addToRequestQueue(stringRequest);
    }


    @Override
    public void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void newAccount(@NonNull Class<? extends Account> type, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void getAccountCopy(@NonNull String accountID, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void saveAccount(@NonNull Account account, @NonNull SaveAccountCallback callback) {
        String requestBody = null;
        try {
            ArrayList<Account> accountsList = new ArrayList<>();
            accountsList.add(account);
            requestBody = IO.serializeAccounts(accountsList);
            System.out.println(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalRequestBody = requestBody;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_ADD_ACCOUNTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Sent to server:\n" + finalRequestBody);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.myContext,
                                (error.getMessage() == null) ? "Error Syncing with Server" : "Error Syncing with Server: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
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
                Map<String, String> params = new HashMap<String, String>();
                final String basicAuth = User.getToken();
                params.put("Authorization", basicAuth);

                return params;
            }
        };

        VolleySingleton.getInstance(MainActivity.myContext).addToRequestQueue(stringRequest);
    }

    @Override
    public void disableAccount(@NonNull String accountID) {

    }

    @Override
    public void deleteAllAccounts() {

    }

    @Override
    public void refreshAccounts(@NonNull LoadAccountsCallback callback) {

    }
}
