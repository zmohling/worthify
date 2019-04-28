package team_10.client.data.source.remote;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import team_10.client.MainActivity;
import team_10.client.constant.PERIOD;
import team_10.client.constant.TYPE;
import team_10.client.constant.URL;
import team_10.client.data.User;
import team_10.client.data.models.Account;
import team_10.client.data.source.AccountsDataSource;
import team_10.client.utility.io.AppExecutors;
import team_10.client.utility.io.IO;
import team_10.client.utility.io.SharedPreferencesManager;
import team_10.client.utility.io.VolleyPostRequest;

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
        VolleyPostRequest request = new VolleyPostRequest(finalRequestBody, URL.URL_GET_ACCOUNTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);

                            if (object.has("accounts")) {
                                List<Account> accounts = IO.deserializeAccounts(response);

                                System.out.println("Fetched from server:\n" + IO.serializeAccounts(accounts));

                                callback.onAccountsLoaded(accounts);

                            } else if (object.has("error") &&
                                    object.getBoolean("error") && User.getToken() != "") {

                                String message = object.getString("message");
                                Toast.makeText(MainActivity.myContext, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        request.execute();
    }


    @Override
    public void getAccount(@NonNull String accountID, @NonNull GetAccountCallback callback) {

    }

    @Override
    public void newAccount(@NonNull TYPE type, @NonNull GetAccountCallback callback) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finalRequestBody = requestBody;
        VolleyPostRequest request = new VolleyPostRequest(requestBody, URL.URL_ADD_ACCOUNTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.has("error") &&
                            object.getBoolean("error")) {  // handle request specific errors

                        String message = object.getString("message");

                        Toast.makeText(MainActivity.myContext, "Error: " + message, Toast.LENGTH_SHORT).show();

                    } else {
                        System.out.println("Successfully sent to server:\n" + finalRequestBody);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        request.execute();
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

    @Override
    public Map<LocalDate, Double> getValues(@NonNull PERIOD period) {
        return null;
    }
}
