package com.cm_project.physio2go.fragmentsRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.LoginActivity;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

public class RegisterBasicInfoFragment extends Fragment {

    private ServerDatabaseDriver server;


    public RegisterBasicInfoFragment() {

    }

    public static RegisterBasicInfoFragment newInstance() {
        RegisterBasicInfoFragment fragment = new RegisterBasicInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         server = new ServerDatabaseDriver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_basicinfo, container, false);

        EditText name_et = view.findViewById(R.id.name_et);
        String name = name_et.getText().toString();
        EditText surname_et = view.findViewById(R.id.surname_et);
        String surname = surname_et.getText().toString();
        EditText username_et = view.findViewById(R.id.username_et);
        String username = username_et.getText().toString();
        EditText password_et = view.findViewById(R.id.password_et);
        String password = password_et.getText().toString();
        EditText confirmPassword_et = view.findViewById(R.id.confirmPassword_et);
        String confirmPassword = confirmPassword_et.getText().toString();
        EditText dateOfBirth_et = view.findViewById(R.id.dateOfBirth_et);
        String dateOfBirth = dateOfBirth_et.getText().toString();
        EditText address_et = view.findViewById(R.id.address);
        String address = address_et.getText().toString();


        TextView backToLogin = view.findViewById(R.id.login_btn);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // checks if username exists
       // boolean usernameExists = server.usernameExists(username);

     //   if (usernameExists == true) {

       // }

        // checks if password == confirmPassword
        if (!password.equals(confirmPassword)) {

        }

        // checks if all inputs are not empty
        if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty()) {

        }

        return view;
    }
}
