package com.cm_project.physio2go.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cm_project.physio2go.LoginActivity;
import com.cm_project.physio2go.MainActivity;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;


public class LoginAsyncTask extends AsyncTask<String, Void, Integer> {

    private String username;
    private String password;
    private Context context;
    private View view;

    public LoginAsyncTask(Context context, View view) {
        this.username = username;
        this.password = password;
        this.context = context;
        this.view = view;
    }

    @Override
    protected Integer doInBackground(String... string) {
        Integer loginCode;
        this.username = string[0];
        this.password = string[1];

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
        LoginActivity loginActivity = (LoginActivity) context;
        View actView = view.findViewById(R.id.login_activity);
        TextView msgLoginTv = view.findViewById(R.id.failed_login_tv);
        switch (loginCode) {
            case LoginActivity.LOGIN_OK:
                loginActivity.getIntent().putExtra("username", username);
                loginActivity.setResult(Activity.RESULT_OK, loginActivity.getIntent());
                loginActivity.finish();
                break;
            case LoginActivity.LOGIN_USER_NOT_FOUND:
                ((EditText) view.findViewById(R.id.username_et)).getText().clear();
                ((EditText) view.findViewById(R.id.password_et)).getText().clear();

                // Set message
                msgLoginTv.setText("User is not registered. Click signup button.");
                msgLoginTv.setVisibility(View.VISIBLE);
                break;
            case LoginActivity.LOGIN_WRONG_PASSWORD:
                ((EditText) view.findViewById(R.id.username_et)).getText().clear();
                ((EditText) view.findViewById(R.id.password_et)).getText().clear();

                // Set message
                msgLoginTv.setText("Username or password incorrect.");
                msgLoginTv.setVisibility(View.VISIBLE);
                break;
            case LoginActivity.LOGIN_CONNECTION_FAILED:
                ((EditText) view.findViewById(R.id.username_et)).getText().clear();
                ((EditText) view.findViewById(R.id.password_et)).getText().clear();

                // Set message
                msgLoginTv.setText("A problem has occurred");
                msgLoginTv.setVisibility(View.VISIBLE);
                break;
            case LoginActivity.LOGIN_NO_INTERNET_CONNECTION:
                ((EditText) view.findViewById(R.id.username_et)).getText().clear();
                ((EditText) view.findViewById(R.id.password_et)).getText().clear();

                MainActivity.showNoInternetSnackbar(actView, "No internet connection!");
                break;
            default:
                break;
        }

        // Disable ProgressBar
        ProgressBar spinLogin = view.findViewById(R.id.spin_login_pb);
        spinLogin.setVisibility(View.GONE);
    }
}


