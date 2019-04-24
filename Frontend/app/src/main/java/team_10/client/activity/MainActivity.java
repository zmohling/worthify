package team_10.client.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import team_10.client.R;
import team_10.client.data.models.Account;
import team_10.client.fragment.DashboardFragment;
import team_10.client.fragment.NewsArticle;
import team_10.client.fragment.NewsFragment;
import team_10.client.fragment.TransactionsFragment;
import team_10.client.object.User;
import team_10.client.settings.SharedPreferencesManager;
import team_10.client.utility.IO;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, TransactionsFragment.OnFragmentInteractionListener, NewsArticle.OnFragmentInteractionListener {

    public static Context myContext;

    private static BottomNavigationView bottomNav;
    Socket serverSocket;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;
            boolean worked = false;

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    f = new NewsFragment();
                    worked = loadFragment(f, "other");
                    break;
                case R.id.navigation_dashboard:
                    f = new DashboardFragment();
                    worked = loadFragment(f, "home");
                    break;
                case R.id.navigation_transactions:
                    f = new TransactionsFragment();
                    worked = loadFragment(f, "other");
                    break;
            }

            return worked;
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

        myContext = this.getApplicationContext();

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
        //loadFragment(new DashboardFragment(), "home");
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("ERROR: Did not close socket.");
            e.printStackTrace();
        }

        /* Write to file */
        IO.writeAccountsToFile(IO.serializeAccounts(User.getAccounts()), getApplicationContext());
    }

    private boolean loadFragment(Fragment fragment, String name) {
        //switching fragment
        //fragment.getId();
        if (fragment != null) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            // 1. Know how many fragments there are in the stack
            final int count = fragmentManager.getBackStackEntryCount();
            // 2. If the fragment is **not** "home type", save it to the stack
            if( name.equals("other") && count == 0 ) {
                fragmentTransaction.addToBackStack(name);
            }
            // Commit !
            fragmentTransaction.commit();
            // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
            // OnBackStackChanged callback
            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    // If the stack decreases it means I clicked the back button
                    if( fragmentManager.getBackStackEntryCount() <= count){
                        // pop all the fragment and remove the listener
                        fragmentManager.popBackStack("other", POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.removeOnBackStackChangedListener(this);
                        // set the home button selected
                        bottomNav.getMenu().getItem(1).setChecked(true);
                    }
                }
            });
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
