package team_10.client.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

import team_10.client.account.*;
import team_10.client.settings.*;
import team_10.client.utility.*;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewFullName, textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewFullName = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);


        //getting the current user
        final User user = SharedPreferencesManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewFullName.setText(user.getFirstName() + " " + user.getLastName());
        textViewEmail.setText(user.getEmail());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPreferencesManager.getInstance(getApplicationContext()).logout();
            }
        });

        // Articles Volley Request
        String urlArticles = "http://cs309-jr-1.misc.iastate.edu:8080/article/getAll";
        StringRequest stringRequest = new StringRequest(Method.GET, urlArticles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject returned = new JSONObject(response);
                            int numArticles = returned.getInt("numArticles");
                            String[] articleURLS = new String[numArticles];
                            int i;
                            for (i = 0; i < numArticles; i++)
                            {
                                try {
                                    JSONObject userJson = returned.getJSONObject("article"+ i);
                                    articleURLS[i] = userJson.getString("url");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);

                            for (int j = 0; j < numArticles; j++) {

                                TableRow row= new TableRow(getApplicationContext());
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                lp.setMargins(10, 10, 10, 10);
                                row.setLayoutParams(lp);
                                TextView tv = new TextView(getApplicationContext());
                                tv.setText(articleURLS[j]);
                                tv.setPadding(10,5,10,5);
                                tv.setTextColor(Color.parseColor("#EDE8D6"));
                                tv.setMaxLines(1);
                                tv.setPadding(0,5,0,5);
                                //tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                //Linkify.addLinks(tv, Linkify.WEB_URLS);
                                row.addView(tv);
                                //row.setVisibility(View.GONE);
                                ll.addView(row,j + 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                });


        // Accounts Volley Request
        String urlAccounts = "https://8b67c89e-e67a-4c42-8bbe-747d4438afc8.mock.pstmn.io/getAccounts";
        final StringRequest accountsStringRequest = new StringRequest(Method.GET, urlAccounts,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject returned = new JSONObject(response);

                            GsonBuilder b = new GsonBuilder();
                            b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
                            b.registerTypeAdapter(Transaction.class, new AbstractTransactionAdapter());
                            b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
                            b.setPrettyPrinting();
                            Gson g = b.create();

                            AccountsWrapper wrapper = g.fromJson(response, AccountsWrapper.class);
                            List<Account> wrapperAccounts = wrapper.getAccounts();

                            List<Account> accounts = user.getAccounts();
                            accounts.addAll(wrapperAccounts);

                            TableLayout ll = (TableLayout) findViewById(R.id.displayLinearAccounts);

                            for (int j = 0; j < accounts.size(); j++) {
                                Account a = accounts.get(j);

                                TableRow row = new TableRow(getApplicationContext());
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                lp.setMargins(10, 10, 10, 10);
                                row.setLayoutParams(lp);
                                TextView tv = new TextView(getApplicationContext());
                                tv.setText("Type: " + a.getClass().getSimpleName() +
                                        ", ID: " + a.getId() + ", Today's Value: " +
                                        a.getValue(LocalDate.now().plusMonths(18)));
                                tv.setPadding(10, 5, 10, 5);
                                tv.setTextColor(Color.parseColor("#EDE8D6"));
                                tv.setMaxLines(1);
                                tv.setPadding(0, 5, 0, 5);
                                row.addView(tv);
                                ll.addView(row, j + 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                });

        // Access the RequestQueue through your singleton class.
        //VolleySingleton.getInstance(this).addToRequestQueue(accountsStringRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        // Button listeners.
        findViewById(R.id.buttonArticles).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScrollView table = (ScrollView) findViewById(R.id.list_scroll);
                        if (table.getVisibility() == View.VISIBLE) {
                            table.setVisibility(View.GONE);
                        } else {
                            table.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
        findViewById(R.id.buttonAccounts).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScrollView table = (ScrollView) findViewById(R.id.list_scroll_accounts);
                        if (table.getVisibility() == View.VISIBLE) {
                            table.setVisibility(View.GONE);
                        } else {
                            table.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        accountsTest();
    }

    public void accountsTest() {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        //b.registerTypeAdapter(Transaction.class, new AbstractTransactionAdapter());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        b.setPrettyPrinting();
        Gson g = b.create();

        // Accounts Testing
        //==================================================
        final User user = SharedPreferencesManager.getInstance(this).getUser();

        Loan carloan = new Loan();
        carloan.setId(1234);
        carloan.addTransaction(LocalDate.now(), 100, 0.04);
        carloan.addTransaction(LocalDate.now().plusYears(1), 1000, 0.08);

        user.addAccount(carloan);

        Loan mortage = new Loan();
        mortage.setId(1235);
        mortage.addTransaction(LocalDate.now().plusMonths(3), 233000, 0.03);

        user.addAccount(mortage);

        //==================================================

        AccountsWrapper a = new AccountsWrapper();
        a.setAccounts(user.getAccounts());


        System.out.println(g.toJson(a, AccountsWrapper.class));
    }
}
