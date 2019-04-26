package team_10.client.utility.io;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import team_10.client.MainActivity;
import team_10.client.data.source.UserRepository;

public class VolleyPostRequest extends StringRequest {

    private String mRequestBody;

    public VolleyPostRequest(@Nullable String requestBody, String url, Response.Listener<String> listener) {
        super(Request.Method.POST, url, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.mRequestBody = requestBody;

    }

    public VolleyPostRequest(@Nullable String requestBody, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, listener, errorListener);

        this.mRequestBody = requestBody;
    }

    public void execute() {
        IO.isConnected(new HostReachableCallback() {
            @Override
            public void onHostReachable() {
                VolleySingleton.getInstance(MainActivity.myContext).addToRequestQueue(VolleyPostRequest.this);
            }
        });
    }

    @Override
    public String getBodyContentType() {
        return String.format("application/json; charset=utf-8");
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, "utf-8");
            return null;
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        final String basicAuth = UserRepository.getInstance().getAuthenticationToken();
        params.put("Authorization", basicAuth);

        return params;
    }
}
