package com.cm_project.physio2go;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

public class LoginActivity extends AppCompatActivity {

    final int REQ_SIGNUP = 1;
    public final int LOGIN_OK = 0;
    public final int LOGIN_USER_NOT_FOUND = 1;
    public final int LOGIN_WRONG_PASSWORD = 2;
    public final int LOGIN_CONNECTION_FAILED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                int loginCode = checkLoginCombination(username, password);
                TextView msgLoginTv = findViewById(R.id.failed_login_tv);
                switch (loginCode) {
                    case LOGIN_OK:
                        getIntent().putExtra("username", username);
                        setResult(RESULT_OK, getIntent());
                        finish();
                        break;
                    case LOGIN_USER_NOT_FOUND:
                        ((EditText) findViewById(R.id.username_et)).getText().clear();
                        ((EditText) findViewById(R.id.password_et)).getText().clear();

                        // Set message
                        msgLoginTv.setText("User is not registered. Click signup button.");
                        msgLoginTv.setVisibility(View.VISIBLE);
                        break;
                    case LOGIN_WRONG_PASSWORD:
                        ((EditText) findViewById(R.id.username_et)).getText().clear();
                        ((EditText) findViewById(R.id.password_et)).getText().clear();

                        // Set message
                        msgLoginTv.setText("Username or password incorrect.");
                        msgLoginTv.setVisibility(View.VISIBLE);
                        break;
                    case LOGIN_CONNECTION_FAILED:
                        ((EditText) findViewById(R.id.username_et)).getText().clear();
                        ((EditText) findViewById(R.id.password_et)).getText().clear();

                        // Set message
                        msgLoginTv.setText("A problem has occurred");
                        msgLoginTv.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int checkLoginCombination(String username, String password) {
        int loginCode;
        ServerDatabaseDriver db = new ServerDatabaseDriver();

        try {
            String passwordDB = db.getPasswordForUsername(username);
            System.out.println(passwordDB);

            if (password.equals(passwordDB)) {
                loginCode = LOGIN_OK;
            } else if (passwordDB == null) {
                loginCode = LOGIN_USER_NOT_FOUND;
            } else if (!password.equals(passwordDB)) {
                loginCode = LOGIN_WRONG_PASSWORD;
            } else {
                loginCode = LOGIN_CONNECTION_FAILED;
            }

        } catch (Exception e) {
            loginCode = LOGIN_CONNECTION_FAILED;
            e.printStackTrace();
            Toast.makeText(this, "ERRO QUALQUER", Toast.LENGTH_LONG).show();
        }

        return loginCode;
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
                        String newUsername = intent.getStringExtra("username");
                        String newPassword = intent.getStringExtra("password");


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
