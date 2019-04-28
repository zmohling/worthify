package team_10.client.utility.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import team_10.client.R;
import team_10.client.data.Article;
import team_10.client.news.NewsArticle;
import team_10.client.utility.io.HostReachableCallback;
import team_10.client.utility.io.IO;
import team_10.client.utility.io.SharedPreferencesManager;
import team_10.client.utility.io.VolleySingleton;

import static team_10.client.constant.URL.ROOT_URL;

public class ArticlesAdapter extends
        RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView votesTextView;
        public ImageView imageView;
        public ImageView downVote;
        public ImageView upVote;
        public Context context;

        public LinearLayout linearLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            titleTextView = (TextView) itemView.findViewById(R.id.contact_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.contact_description);
            imageView = (ImageView) itemView.findViewById(R.id.contact_picture);
            votesTextView = (TextView) itemView.findViewById(R.id.contact_votes_num);
            downVote = (ImageView) itemView.findViewById(R.id.contact_downvote);
            upVote = (ImageView) itemView.findViewById(R.id.contact_upvote);
            context = itemView.getContext();

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

    // Store a member variable for the contacts
    private List<Article> mArticles;
    private FragmentManager fragmentManager;

    // Pass in the contact array into the constructor
    public ArticlesAdapter(List<Article> articles, FragmentManager fragmentManagers) {
        mArticles = articles;
        fragmentManager = fragmentManagers;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context contexts = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(contexts);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Article article = mArticles.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(article.getUrl());
        viewHolder.titleTextView.setText(article.getTitle());
        viewHolder.descriptionTextView.setText(article.getDescription());
        viewHolder.votesTextView.setText("Votes: " + article.getArticleVotes());

        ImageView imageViewHolder = viewHolder.imageView;

        if (!article.getPictureUrl().equals("null")) {
            Picasso.get().load(article.getPictureUrl()).into(imageViewHolder);
        } else {
            imageViewHolder.setVisibility(View.GONE);
        }

        viewHolder.downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlArticles = ROOT_URL + "article/downvote/" + SharedPreferencesManager.getUser().getID() + "/" + article.getArticleID();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArticles,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject returned = new JSONObject(response);
                                    String currentVote = returned.getString("currentVote");
                                    String articleVotes = returned.getString("articleVotes");
                                    viewHolder.votesTextView.setText("Votes: " + articleVotes);
                                    if (currentVote.equals("-1"))
                                    {
                                        viewHolder.downVote.setImageResource(R.drawable.downvote_vote);
                                    }
                                    else
                                    {
                                        viewHolder.downVote.setImageResource(R.drawable.downvote_no_vote);
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

                IO.isConnected(new HostReachableCallback() {
                    @Override
                    public void onHostReachable() {
                        VolleySingleton.getInstance(viewHolder.context).addToRequestQueue(stringRequest);
                    }
                });
            }
        });

        viewHolder.upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlArticles = ROOT_URL + "article/upvote/" + SharedPreferencesManager.getUser().getID() + "/" + article.getArticleID();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlArticles,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject returned = new JSONObject(response);
                                    String currentVote = returned.getString("currentVote");
                                    String articleVotes = returned.getString("articleVotes");
                                    viewHolder.votesTextView.setText("Votes: " + articleVotes);
                                    if (currentVote.equals("1"))
                                    {
                                        viewHolder.downVote.setImageResource(R.drawable.upvote_vote);
                                    }
                                    else
                                    {
                                        viewHolder.downVote.setImageResource(R.drawable.upvote_no_vote);
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

                IO.isConnected(new HostReachableCallback() {
                    @Override
                    public void onHostReachable() {
                        VolleySingleton.getInstance(viewHolder.context).addToRequestQueue(stringRequest);
                    }
                });
            }
        });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsArticle newsArticle = new NewsArticle().newInstance(article.getUrl());

                //Bundle args = new Bundle();
                //args.putString("url", article.getUrl());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.fragment_container, newsArticle);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}

