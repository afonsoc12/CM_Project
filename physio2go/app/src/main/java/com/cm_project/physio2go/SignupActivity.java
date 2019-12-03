package com.cm_project.physio2go;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText getEditTextPass;
    private EditText editTextConfirm;
    private TextView backLogin;
    private String existe = "";

    // TODO: SIGNUP precis de ser editado
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // this.user = new User();
        this.editTextNome = (EditText) findViewById(R.id.editTextNome);
        this.getEditTextPass = (EditText) findViewById(R.id.editTextPass);

        editTextConfirm = (EditText) findViewById(R.id.editTextPassConfirm);
        backLogin = (TextView) findViewById(R.id.back);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(login);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                this.editTextNome.setText(bundle.getString("username"));
                this.getEditTextPass.setText(bundle.getString("password"));
            }
        }
    }

    public void save(View view) {
        existe = "";
        if (editTextConfirm.getText().toString().equals(getEditTextPass.getText().toString())) {
            //  this.user.setUsername(this.editTextNome.getText().toString());
            //this.user.setPassword(this.getEditTextPass.getText().toString());
            //existe = this.user.save();
            System.out.println(existe);
            if (existe.equals("novo")) {
                Toast.makeText(this, "Registado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                String messageName = editTextNome.getText().toString();
                String messagePass = getEditTextPass.getText().toString();

                intent.putExtra("name", messageName);
                intent.putExtra("pass", messagePass);

                startActivity(intent);
            } else if (existe.equals("ja existe")) {
                Toast.makeText(this, "Já existe este utilizador", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Dados Incorretos", Toast.LENGTH_LONG).show();
        }
    }
}
