package team_10.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import team_10.client.R;
import team_10.client.fragment.DashboardFragment;
import team_10.client.fragment.NewsArticle;
import team_10.client.fragment.NewsFragment;
import team_10.client.fragment.TransactionsFragment;
import team_10.client.object.User;
import team_10.client.object.account.Account;
import team_10.client.settings.SharedPreferencesManager;
import team_10.client.utility.IO;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, TransactionsFragment.OnFragmentInteractionListener, NewsArticle.OnFragmentInteractionListener {

    private static BottomNavigationView bottomNav;
    //private static LinearLayout hidelayout;
    //public BottomNavigationView bottomNav;

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

    public static void hideBottomNavigationView() {
        bottomNav.setVisibility(View.GONE);
    }

    public static void showBottomNavigationView() {
        bottomNav.setVisibility(View.VISIBLE);
    }

    /* USE THIS FOR LOGIN / MAIN SCREEN FLOW AND INITS */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            List<Account> aFromFile = IO.deserializeAccounts(IO.readAccountsFromFile(getApplicationContext()));
            if (aFromFile != null)
                User.setAccounts(aFromFile);
            else
                IO.getAccountsFromRemote(getApplicationContext());
            System.out.println(IO.serializeAccounts(User.getAccounts()));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNav = navigation;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        //loadFragment(new DashboardFragment());
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* Write to file */
        IO.writeAccountsToFile(IO.serializeAccounts(User.getAccounts()), getApplicationContext());
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        //fragment.getId();
        if (fragment != null) {
            String fragmentTag = fragment.getClass().getSimpleName();
            FragmentManager fragmentManager= getSupportFragmentManager();

            boolean fragmentPopped = fragmentManager
                    .popBackStackImmediate(fragmentTag , 0);

            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {

                fragmentManager.beginTransaction()
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
            if (fragmentTag.equals("DashboardFragment"))
            {
                //bottomNav.setSelectedItemId(R.id.navigation_dashboard);
            }
            else if (fragmentTag.equals("TransactionsFragment"))
            {
                //bottomNav.setSelectedItemId(R.id.navigation_transactions);
            }
            else if (fragmentTag.equals("NewsFragment"))
            {
                //bottomNav.setSelectedItemId(R.id.navigation_news);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate();
            Fragment fragment = (Fragment) manager.findFragmentById(R.id.fragment_container);
            String fragmentTag = fragment.getClass().getSimpleName();
            if (fragmentTag.equals("DashboardFragment"))
            {
                Menu menu = bottomNav.getMenu();

                for (int i = 0, size = menu.size(); i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    item.setChecked(item.getItemId() == 2131296397); //dashboard item id
                }
            }
            else if (fragmentTag.equals("TransactionsFragment"))
            {
                Menu menu = bottomNav.getMenu();

                for (int i = 0, size = menu.size(); i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    item.setChecked(item.getItemId() == 2131296400);
                }
            }
            else if (fragmentTag.equals("NewsFragment"))
            {
                Menu menu = bottomNav.getMenu();

                for (int i = 0, size = menu.size(); i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    int test = item.getItemId();
                    item.setChecked(item.getItemId() == 2131296399); //news item id
                }
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
