package team_10.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.Socket;
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
    Socket serverSocket;


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

        new SocketConnection().execute();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNav = navigation;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        loadFragment(new DashboardFragment());
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* Write to file */
        IO.writeAccountsToFile(IO.serializeAccounts(User.getAccounts()), getApplicationContext());
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

    private class SocketConnection extends AsyncTask<Void, Void, Void> {
        String serverHostname = "cs309-jr-1.misc.iastate.edu";
        int serverPortNumber = 4444;

        boolean isConnected = false;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                serverSocket = new Socket(serverHostname, serverPortNumber);
                isConnected = true;

            } catch (Exception e) {
                isConnected = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isConnected) {
                Toast.makeText(getApplicationContext(), "Connection Successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Connection Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
