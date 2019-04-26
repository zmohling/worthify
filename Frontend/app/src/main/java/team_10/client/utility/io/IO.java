package team_10.client.utility.io;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team_10.client.MainActivity;
import team_10.client.constant.URL;
import team_10.client.dashboard.DashboardFragment;
import team_10.client.data.User;
import team_10.client.data.models.Account;
import team_10.client.utility.AccountsWrapper;
import team_10.client.utility.adapter.AbstractAccountAdapter;

/**
 * Class for input and output with the server/database.
 */
public class IO {
    public static String filename = "accounts_store";

    public static boolean isConnected() {
        if (isNetworkAvailable() && MainActivity.socket.isConnected()) {
            return true;
        } else {
            Toast.makeText(MainActivity.myContext, "Cannot Reach Server", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) MainActivity.myContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Writes the user accounts to a file.
     *
     * @param accounts user accounts
     * @param context  context
     * @return int of success
     */
    public static int writeAccountsToFile(String accounts, Context context) {
        String fileContents = accounts;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    /**
     * Reads a user's accounts from a file.
     *
     * @param context context
     * @return String of accounts
     */
    public static String readAccountsFromFile(Context context) {
        StringBuffer accounts = new StringBuffer();
        try {
            FileInputStream stream = context.openFileInput(filename);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader bufferReader = new BufferedReader(streamReader);

            String readString = bufferReader.readLine();
            while (readString != null) {
                accounts.append(readString);
                readString = bufferReader.readLine();
            }

            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof FileNotFoundException) {
                File directory = context.getFilesDir();
                File file = new File(directory, filename);
            }
        }

        return accounts.toString();
    }

    /**
     * Deletes the user's accounts file.
     *
     * @param context context
     */
    public static void deleteAccountsFile(Context context) {
        context.deleteFile(filename);
    }

    /**
     * Serializes the user's accounts.
     *
     * @param accounts user's accounts
     * @return string of serialized accounts
     */
    public static String serializeAccounts(List<Account> accounts) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        b.setPrettyPrinting();
        Gson g = b.create();

        AccountsWrapper a = new AccountsWrapper();
        a.setAccounts(accounts);

        return (g.toJson(a, AccountsWrapper.class));
    }

    /**
     * Deserializes the user's accounts.
     *
     * @param accounts user's accounts
     * @return list of accounts
     */
    public static List<Account> deserializeAccounts(String accounts) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        b.setPrettyPrinting();
        Gson g = b.create();

        AccountsWrapper a = g.fromJson(accounts, AccountsWrapper.class);

        if (a == null) {
            return null;
        } else {
            return (a.getAccounts());
        }
    }

    /**
     * Sends a user's account to the server.
     *
     * @param account account
     * @param context context
     */
    public static void sendAccountToRemote(final Account account, final Context context) {
        String requestBody = null;
        try {
            ArrayList<Account> accountsList = new ArrayList<>();
            accountsList.add(account);
            requestBody = serializeAccounts(accountsList);
            System.out.println(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalRequestBody = requestBody;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_ADD_ACCOUNTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Gets a user's accounts from the server.
     *
     * @param context context
     */
    public static void getAccountsFromRemote(final Context context) {
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
                                List<Account> l = deserializeAccounts(response);

                                for (int i = 0; i < l.size(); i++)
                                    // TODO
                                    //User.addAccount(l.get(i));

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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        // TODO: User.setAccounts(returnVal);
    }
}
