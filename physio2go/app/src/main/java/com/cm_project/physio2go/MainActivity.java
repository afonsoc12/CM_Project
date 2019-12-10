package com.cm_project.physio2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cm_project.physio2go.classes.Plan;
import com.cm_project.physio2go.databaseDrivers.LocalDatabase;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String loggedInUsername;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_NAME = "loggedin_username";
    final int REQ_LOGIN = 1;
    LocalDatabase local;

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
            inflateMainActivity();
            updateLocalDatabase(this.loggedInUsername);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode){
            case REQ_LOGIN:
                if (resultCode == 1){ // If login successfull
                    if (intent!= null) {
                        this.loggedInUsername = intent.getStringExtra("username");
                        saveLoggedInUsername(this.loggedInUsername);
                        inflateMainActivity();
                        updateLocalDatabase(this.loggedInUsername);
                        getLocalDatabase();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void inflateMainActivity() {
        setContentView(R.layout.activity_main);

        // Define a new toolbar for this activity
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Instanciate list fragment
        // TODO instanciar frag
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

    /**
     * Extracts information from the server database and updates the local database
     * @param username
     */
    public void updateLocalDatabase(String username){
        ServerDatabaseDriver server = new ServerDatabaseDriver();

        ArrayList<Plan> plansFromServer = server.getPlansOfUser(username);

        local = new LocalDatabase(getApplicationContext());

        local.updatePlans(plansFromServer);

    }

    public void getLocalDatabase(){
        ArrayList<Plan> plansFromLocalDatabase = local.getPlansOfUser();
    }
}

