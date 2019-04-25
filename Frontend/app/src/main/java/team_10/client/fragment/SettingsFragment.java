package team_10.client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import team_10.client.R;
import team_10.client.activity.MainActivity;
import team_10.client.constant.URL;
import team_10.client.object.User;
import team_10.client.object.account.Account;
import team_10.client.object.account.Transaction;
import team_10.client.settings.SharedPreferencesManager;
import team_10.client.utility.IO;
import team_10.client.utility.TransactionsAdapter;
import team_10.client.utility.VolleySingleton;

import static team_10.client.settings.SharedPreferencesManager.getUser;

/**
 * A {@link Fragment} subclass that displays the users email and allows the user to change email and password or logout.
 * Activities that contain this fragment must implement the
 * {@link TransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView firstName = view.findViewById(R.id.text_view_profile_first_name);
        firstName.setText("" + getUser().getFirstName());
        TextView lastName = view.findViewById(R.id.text_view_profile_last_name);
        lastName.setText("" + getUser().getLastName());
        TextView emailName = view.findViewById(R.id.text_view_profile_email);
        emailName.setText("" + getUser().getEmail());
        TextView editEmail = view.findViewById(R.id.text_view_change_email);
        editEmail.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                editEmailModal();
            }
        });
        TextView editPassword = view.findViewById(R.id.text_view_change_password);
        editPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                editPasswordModal();
            }
        });
        Button logoutButton = view.findViewById(R.id.buttonLogoutSettings);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity parent = getActivity();

                switch (v.getId()) {
                    case R.id.buttonLogoutSettings:
                        IO.deleteAccountsFile(parent.getApplicationContext());
                        User.setAccounts(null);
                        User.setToken(null);
                        SharedPreferencesManager.getInstance(parent.getApplicationContext()).logout();
                        parent.finish();
                        break;
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Method to have a modal popup for editing the user's email.
     */
    public void editEmailModal() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View parent = this.getView();
        final View popupView = inflater.inflate(R.layout.modal_edit_email, null);

        // Create a window for popupView
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);  // Ensure keyboard does not resize
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.modal_edit_email_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(100);
        }

        ((Button) popupView.findViewById(R.id.modal_edit_email_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) popupView.findViewById(R.id.modal_edit_email_email);
                final EditText password = (EditText) popupView.findViewById(R.id.modal_edit_email_password);
                EditText newEmail = (EditText) popupView.findViewById(R.id.modal_edit_email_new_email);
                final String emailString = email.getText().toString();
                final String passwordString = password.getText().toString();
                final String newEmailString = newEmail.getText().toString();

                Pattern emailRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=" +
                        "?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                        "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a" +
                        "-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0" +
                        "c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher m = emailRegex.matcher(email.getText());
                Matcher nm = emailRegex.matcher(newEmail.getText());

                //validating inputs
                if (TextUtils.isEmpty(emailString) || !m.matches()) {
                    email.setError("Invalid or Missing Email");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Invalid or Missing Password");
                    password.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newEmailString) || !nm.matches()) {
                    newEmail.setError("Invalid or Missing New Email");
                    newEmail.requestFocus();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("email", emailString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("password", passwordString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("changedEmail", newEmailString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = json.toString();

                //if everything is fine
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_EDIT_EMAIL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);

                                    //if no error in response
                                    if (!obj.getBoolean("error")) {

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
                                        SharedPreferencesManager.getInstance(getContext()).logout();
                                        SharedPreferencesManager.getInstance(getContext()).userLogin(user);

                                        //starting the profile activity
                                        Toast.makeText(getContext(), "Email Successfully Changed", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Unsuccessful Change: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Unsuccessful Change", Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),
                                        (error.getMessage() == null) ? "Unsuccessful Login" : "Unsuccessful Login: " + error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                password.setText("");
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
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        final String basicAuth = User.getToken();
                        params.put("Authorization", basicAuth);

                        return params;
                    }
                };

                VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }
        });
    }
    /**
     * Method to have a modal popup for editing the user's password.
     */
    public void editPasswordModal()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View parent = this.getView();
        final View popupView = inflater.inflate(R.layout.modal_edit_password, null);

        // Create a window for popupView
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);  // Ensure keyboard does not resize
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.modal_edit_password_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(100);
        }

        ((Button) popupView.findViewById(R.id.modal_edit_password_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) popupView.findViewById(R.id.modal_edit_password_email);
                final EditText password = (EditText) popupView.findViewById(R.id.modal_edit_password_password);
                EditText newPassword = (EditText) popupView.findViewById(R.id.modal_edit_password_new_password);
                final String emailString = email.getText().toString();
                final String passwordString = password.getText().toString();
                final String newPasswordString = newPassword.getText().toString();

                Pattern emailRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=" +
                        "?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                        "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a" +
                        "-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0" +
                        "c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher m = emailRegex.matcher(email.getText());

                //validating inputs
                if (TextUtils.isEmpty(emailString) || !m.matches()) {
                    email.setError("Invalid or Missing Email");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Invalid or Missing Password");
                    password.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPasswordString)) {
                    newPassword.setError("Invalid or Missing New Email");
                    newPassword.requestFocus();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("email", emailString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("password", passwordString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("changedPassword", newPasswordString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = json.toString();

                //if everything is fine
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_EDIT_PASSWORD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);

                                    //if no error in response
                                    if (!obj.getBoolean("error")) {

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
                                        SharedPreferencesManager.getInstance(getContext()).logout();
                                        SharedPreferencesManager.getInstance(getContext()).userLogin(user);

                                        //starting the profile activity
                                        Toast.makeText(getContext(), "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Unsuccessful Change: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Unsuccessful Change", Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),
                                        (error.getMessage() == null) ? "Unsuccessful Login" : "Unsuccessful Login: " + error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                password.setText("");
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
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        final String basicAuth = User.getToken();
                        params.put("Authorization", basicAuth);

                        return params;
                    }
                };

                VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
