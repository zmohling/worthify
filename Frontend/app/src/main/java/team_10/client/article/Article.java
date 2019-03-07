package team_10.client.article;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import team_10.client.utility.VolleySingleton;

public class Article {
    private String url;
    private String title;
    private String description;

    public Article(String name) {
        url = name;
    }

    public String getUrl() {
        return url;
    }

    private static int lastContactId = 0;

    public static ArrayList<Article> createArticlesList(final Activity activity, Context context) {
        final ArrayList<Article> articles = new ArrayList<Article>();

        String urlArticles = "http://cs309-jr-1.misc.iastate.edu:8080/article/getAll";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArticles,
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
                                    articles.add(new Article(userJson.getString("url")));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

        return articles;
    }
}
