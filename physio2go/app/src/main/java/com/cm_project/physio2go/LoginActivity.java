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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = findViewById(R.id.back);
        Button login = findViewById(R.id.loginPlanos);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getBaseContext(), SignupActivity.class);
                startActivityForResult(registerIntent, REQ_SIGNUP);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                boolean loginValid = checkLoginCombination(username, password);

                if (loginValid) {
                    getIntent().putExtra("username", username);
                    setResult(1, getIntent());
                    finish();
                } else {
                    // TODO message of not valid login
                    // Clear username and password fields
                    ((EditText) findViewById(R.id.username)).getText().clear();
                    ((EditText) findViewById(R.id.password)).getText().clear();
                    //setResult(0, getIntent());
                    //finish();
                }
            }
        });
    }

    private boolean checkLoginCombination(String username, String password) {
        boolean isLoginValid = false;

        ServerDatabaseDriver db = new ServerDatabaseDriver();

        try {
            String passwordDB = db.getPasswordForUsername(username);
            System.out.println(passwordDB);

            if (password.equals(passwordDB)) {
                isLoginValid = true;
            } else if (passwordDB == null) {
                Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Username or password not matches", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ERRO QUALQUER", Toast.LENGTH_LONG).show();
        }

        return isLoginValid;
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
                if (resultCode == 1) {
                    if (intent != null) {
                        String newUsername = intent.getStringExtra("username");
                        String newPassword = intent.getStringExtra("password");


                        // Autofill login fields
                        if (newUsername != null && newPassword != null) {
                            ((EditText) findViewById(R.id.username)).setText(newUsername);
                            ((EditText) findViewById(R.id.password)).setText(newPassword);
                        }

                    }
                }
                break;
            default:
                break;
        }
    }
}
