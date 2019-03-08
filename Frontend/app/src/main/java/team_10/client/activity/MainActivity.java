package team_10.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import team_10.client.R;
import team_10.client.fragment.DashboardFragment;
import team_10.client.fragment.NewsFragment;
import team_10.client.fragment.TransactionsFragment;
import team_10.client.settings.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, TransactionsFragment.OnFragmentInteractionListener {

    public static User user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    f = new NewsFragment();
                    break;
                case R.id.navigation_dashboard:
                    f = new DashboardFragment();
                    break;
                case R.id.navigation_transactions:
                    f = new TransactionsFragment();
                    break;
            }

            return loadFragment(f);
        }
    };

    /* USE THIS FOR LOGIN / MAIN SCREEN FLOW AND INITS */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
             user = SharedPreferencesManager.getInstance(this).getUser();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        loadFragment(new DashboardFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
