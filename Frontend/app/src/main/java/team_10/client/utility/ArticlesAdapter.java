package team_10.client.utility;

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

import com.squareup.picasso.Picasso;

import java.util.List;

import team_10.client.R;
import team_10.client.object.Article;
import team_10.client.fragment.NewsArticle;

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
        public ImageView imageView;

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
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

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

            ImageView imageViewHolder = viewHolder.imageView;

            if (!article.getPictureUrl().equals("null")) {
                Picasso.get().load(article.getPictureUrl()).into(imageViewHolder);
            }
            else
            {
                imageViewHolder.setVisibility(View.GONE);
            }

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
