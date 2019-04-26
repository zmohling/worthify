package team_10.client.login_and_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.constant.URL;
import team_10.client.data.User;
import team_10.client.utility.io.HostReachableCallback;
import team_10.client.utility.io.IO;
import team_10.client.utility.io.SharedPreferencesManager;
import team_10.client.utility.io.VolleySingleton;

/**
 * This activity allows users to register an account if they don't have on yet.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                try {
                    registerUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        //if user pressed on continue without account
        findViewById(R.id.nonUserLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen

                //creating a new user object
                User user = new User(
                        -1,
                        "user",
                        "test",
                        "sample@example.com",
                        1,
                        ""
                );

                SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void registerUser() throws JSONException {
        List<String> name = new ArrayList<String>(Arrays.asList(editTextName.getText().toString().trim().split("\\s+")));
        ((ArrayList<String>) name).ensureCapacity(2);


        StringBuffer lastNameBuffer = new StringBuffer();

        for (int i = 1; i < name.size(); i++) {

            if (i != 1)
                lastNameBuffer.append(" ");

            lastNameBuffer.append(name.get(i));

        }

        final String firstName = name.get(0);
        final String lastName = lastNameBuffer.toString();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString();
        final String confirmPassword = editTextConfirmPassword.getText().toString();

        Pattern emailRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=" +
                "?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a" +
                "-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0" +
                "c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = emailRegex.matcher(editTextEmail.getText());

        //first we will do the validations
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            editTextName.setError("Invalid or Missing Name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !m.matches()) {
            editTextEmail.setError("Invalid or Missing Email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Invalid or Missing Password");
            editTextPassword.requestFocus();
            return;
        }

        if (!(TextUtils.equals(password, confirmPassword))) {
            editTextPassword.setError(null);
            editTextConfirmPassword.setError("Passwords Do Not Match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        JSONObject json = new JSONObject();
        json.put("lastName", lastName);
        json.put("firstName", firstName);
        json.put("email", email);
        json.put("password", password);
        json.put("type", 0);
        final String requestBody = json.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("lastName"),
                                        userJson.getString("firstName"),
                                        userJson.getString("email"),
                                        userJson.getInt("type"),
                                        userJson.getString("authorization")
                                );

                                //storing the user in shared preferences
                                SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            editTextEmail.setError("Email is Taken");
                            editTextEmail.requestFocus();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };

        IO.isConnected(new HostReachableCallback() {
            @Override
            public void onHostReachable() {
                VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
            }
        });
    }
}
