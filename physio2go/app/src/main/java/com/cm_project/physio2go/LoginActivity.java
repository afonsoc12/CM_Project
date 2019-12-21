package com.cm_project.physio2go;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cm_project.physio2go.AsyncTasks.LoginAsyncTask;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

public class LoginActivity extends AppCompatActivity {

    final int REQ_SIGNUP = 1;
    public final static int LOGIN_OK = 0;
    public final static int LOGIN_USER_NOT_FOUND = 1;
    public final static int LOGIN_WRONG_PASSWORD = 2;
    public final static int LOGIN_CONNECTION_FAILED = 3;
    public final static int LOGIN_NO_INTERNET_CONNECTION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check connection
        boolean hasConnection = MainActivity.isNetworkAvilable(this);
        if (!hasConnection) {
            MainActivity.showNoInternetSnackbar(findViewById(R.id.login_activity), "No internet connection!");
        }

        TextView register = findViewById(R.id.register_btn);
        Button login = findViewById(R.id.login_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivityForResult(registerIntent, REQ_SIGNUP);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username_et)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_et)).getText().toString();

                View view = findViewById(R.id.login_activity);
                new LoginAsyncTask(LoginActivity.this, view).execute(username, password);
            }
        });

        // Disable default Progress Bar
        ProgressBar spinLogin = findViewById(R.id.spin_login_pb);
        spinLogin.setVisibility(View.GONE);
    }


    /**
     * Runs when signup acitvity is concluded
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQ_SIGNUP:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        Patient newPatient = (Patient) intent.getSerializableExtra(RegisterActivity.NEW_PATIENT_ARG);

                        // Save to database
                        ServerDatabaseDriver db = new ServerDatabaseDriver();
                        db.insertNewPatient(newPatient);

                        // Get username and password
                        String newUsername = newPatient.getUsername();
                        String newPassword = newPatient.getPassword();

                        // Autofill login fields
                        if (newUsername != null && newPassword != null) {
                            ((EditText) findViewById(R.id.username_et)).setText(newUsername);
                            ((EditText) findViewById(R.id.password_et)).setText(newPassword);
                        }

                    }
                }
                break;
            default:
                break;
        }
    }

}
