package com.cm_project.physio2go;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.classes.Plan;
import com.cm_project.physio2go.databaseDrivers.LocalDatabase;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String loggedInUsername;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_NAME = "loggedin_username";
    final int REQ_LOGIN = 1;
    LocalDatabase local;

    protected static boolean isNetworkAvilable(Context context) {
        boolean isNetworkAvilable = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable() && info.isConnected()) {
            isNetworkAvilable = true;
        }
        return isNetworkAvilable;
    }

    public static void showNoInternetSnackbar(View v, String message) {

        Snackbar snackBar = Snackbar
                .make(v, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        snackBar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO logout;
        deleteLoggedInUsername();

        this.loggedInUsername = checkLoggedInUsername();

        // There is no login
        if (loggedInUsername == null) {
            // Start login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQ_LOGIN);
        } else {
            boolean serverUpdated = updateLocalDatabase(this.loggedInUsername);
            inflateMainActivity(serverUpdated);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main_activity items for use in the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);

        ActionBar ab = getSupportActionBar();

        //ab.setHomeAsUpIndicator(R.drawable.ic_action_app);

        //Show the icon - selecting "home" returns a single level
        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setTitle("Example");

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles right menu_main_activity options
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.logout_btn: // Removes login from sharedprefs and prompts login activity
                deleteLoggedInUsername();
                Intent loginAgain = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(loginAgain, REQ_LOGIN);

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveLoggedInUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_NAME, username);
        editor.apply();
    }

    public String checkLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString(LOGIN_NAME, null);
        System.out.println(loggedInUsername);
        return loggedInUsername;
    }

    public void deleteLoggedInUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQ_LOGIN:
                if (resultCode == RESULT_OK) { // If login successfull
                    if (intent != null) {
                        this.loggedInUsername = intent.getStringExtra("username");
                        saveLoggedInUsername(this.loggedInUsername);
                        boolean serverUpdated = updateLocalDatabase(this.loggedInUsername);
                        inflateMainActivity(serverUpdated);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getLocalDatabase() {
        ArrayList<Plan> plansFromLocalDatabase = local.getPlansOfUser();
    }

    private void inflateMainActivity(boolean isServerUpdated) {
        setContentView(R.layout.activity_main);

        // Define a new toolbar for this activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get plans of user from local DB
        ArrayList<Plan> plansOfUser = local.getPlansOfUser();

        // Instanciate list fragment
        Fragment plansFragment = PlansListFragment.newInstance(plansOfUser);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_list_placeholder, plansFragment);
        //ft.addToBackStack(PlansListFragment.PLAN_LIST_FRAGMENT_TAG);
        ft.commit();

        // If server was not update, show Snackbar
        if (!isServerUpdated) {
            showNoInternetSnackbar(findViewById(R.id.main_activity), "Could not synchronize with the Server.");
        }
    }

    /**
     * Extracts information from the server database and updates the local database
     * @param username
     */
    public boolean updateLocalDatabase(String username) {
        // Persist on local database
        local = new LocalDatabase(getApplicationContext());

        boolean isNetAvailable = isNetworkAvilable(this);

        if (isNetAvailable) {
            // Retrieve data from server
            ServerDatabaseDriver server = new ServerDatabaseDriver();

            ArrayList<Plan> plansFromServer = server.getPlansOfUser(username);
            Patient patient = server.getPatientDetails(username);

            // Delete files from localDatabase
            local.delete();

            // Updates plans and exercises
            local.updatePlans(plansFromServer);
            local.updatePatientDetails(patient);

            return true;
        } else {
            return false;
        }
    }
}

