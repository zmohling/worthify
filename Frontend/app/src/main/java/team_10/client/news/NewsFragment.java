package team_10.client.news;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import team_10.client.R;
import team_10.client.data.Article;
import team_10.client.utility.adapter.ArticlesAdapter;
import team_10.client.utility.io.HostReachableCallback;
import team_10.client.utility.io.IO;
import team_10.client.utility.io.SharedPreferencesManager;
import team_10.client.utility.io.VolleySingleton;

import static team_10.client.constant.URL.ROOT_URL;

/**
 * A {@link Fragment} subclass to display cards of news articles that can be clicked on.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ArrayList<Article> articles;
    ArticlesAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout pullToRefresh;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Method to get the user's articles from the server. Is recalled on pull down to refresh.
     */
    public void editArticles()
    {

        String urlArticles = ROOT_URL + "article/getPersonal/" + SharedPreferencesManager.getUser().getID();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArticles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject returned = new JSONObject(response);
                            int numArticles = returned.getInt("numArticles");
                            int i;
                            for (i = 0; i < numArticles; i++) {
                                try {
                                    JSONObject userJson = returned.getJSONObject("article"+ i);
                                    articles.add(new Article(URLDecoder.decode(userJson.getString("title"), "UTF-8"), URLDecoder.decode(userJson.getString("description"), "UTF-8"), userJson.getString("pictureUrl"), userJson.getString("url"), userJson.getString("id"), userJson.getString("votes"), userJson.getString("vote")));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            Comparator<Article> articleComparator = new Comparator<Article>() {
                                @Override
                                public int compare(Article o1, Article o2) {
                                    int value;
                                    int o1val;
                                    int o2val;
                                    o1val = Integer.parseInt(o1.getArticleVotes());
                                    o2val = Integer.parseInt(o2.getArticleVotes());
                                    value = o1val - o2val;
                                    return value;
                                }
                            };
                            articles.sort(articleComparator);
                            Collections.reverse(articles);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                });

        IO.isConnected(new HostReachableCallback() {
            @Override
            public void onHostReachable() {
                VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }

            @Override
            public void onHostUnreachable() {

            }
        });
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvArticles);

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        articles = new ArrayList<Article>();
        adapter = new ArticlesAdapter(articles, getFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editArticles();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                articles.clear();
                editArticles();
                pullToRefresh.setRefreshing(false);
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
