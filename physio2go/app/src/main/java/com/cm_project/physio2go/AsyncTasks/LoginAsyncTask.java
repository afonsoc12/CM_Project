package com.cm_project.physio2go.AsyncTasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.cm_project.physio2go.LoginActivity;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;


public class LoginAsyncTask extends AsyncTask<String, Void, Integer> {

    private String username;
    private String password;
    private ILoginAsyncTask returnInterface;
    private View view;

    public LoginAsyncTask(String username, String password, LoginActivity activity, View view) {
        this.username = username;
        this.password = password;
        this.returnInterface = activity;
        this.view = view;
    }

    @Override
    protected Integer doInBackground(String... string) {
        Integer loginCode;

        ServerDatabaseDriver db = new ServerDatabaseDriver();
        loginCode = db.checkLoginCombination(username, password);

        return loginCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Enable ProgressBar
        ProgressBar spinLogin = view.findViewById(R.id.spin_login_pb);
        spinLogin.setVisibility(View.VISIBLE);
        spinLogin.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Integer loginCode) {
        super.onPostExecute(loginCode);
        // Disable ProgressBar
        ProgressBar spinLogin = view.findViewById(R.id.spin_login_pb);
        spinLogin.setVisibility(View.GONE);

        // Return to activity
        returnInterface.loginValidationUIUpdate(loginCode, this.username);
    }
}


