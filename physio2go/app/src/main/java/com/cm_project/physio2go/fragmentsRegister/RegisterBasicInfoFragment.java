package com.cm_project.physio2go.fragmentsRegister;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

public class RegisterBasicInfoFragment extends Fragment {

    private ServerDatabaseDriver server;
    private RegisterBasicInfoListener listener;

    public RegisterBasicInfoFragment() {

    }

    public static RegisterBasicInfoFragment newInstance() {
        RegisterBasicInfoFragment fragment = new RegisterBasicInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        server = new ServerDatabaseDriver();

        View view = inflater.inflate(R.layout.fragment_register_basicinfo, container, false);


        TextView backToLogin = view.findViewById(R.id.login_btn);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });


        Button next = view.findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean usernameOk = true;
                boolean passwordOk = true;
                boolean notEmpty = true;

                String name = ((EditText) view.findViewById(R.id.name_et)).getText().toString();
                String surname = ((EditText) view.findViewById(R.id.surname_et)).getText().toString();
                String username = ((EditText) view.findViewById(R.id.username_et)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.password_et)).getText().toString();
                String confirmPassword = ((EditText) view.findViewById(R.id.confirmPassword_et)).getText().toString();
                String dateOfBirth = ((EditText) view.findViewById(R.id.dateOfBirth_et)).getText().toString();
                String address = ((EditText) view.findViewById(R.id.address_et)).getText().toString();


                // checks if username exists
                boolean usernameExists = server.usernameExists(username);

                if (usernameExists == true) {
                    TextView msgUsernameTv = view.findViewById(R.id.failed_username_tv);
                    msgUsernameTv.setText("This username already exists.");
                    msgUsernameTv.setVisibility(View.VISIBLE);
                    usernameOk = false;
                }

                // checks if password == confirmPassword
                if (!password.equals(confirmPassword)) {
                    TextView msgPasswordTv = view.findViewById(R.id.failed_password_tv);
                    msgPasswordTv.setText("Confirm password must match password.");
                    msgPasswordTv.setVisibility(View.VISIBLE);
                    passwordOk = false;
                }

                // checks if all inputs are not empty
                if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty()) {
                    TextView msgEmptyTv = view.findViewById(R.id.failed_empty_tv);
                    msgEmptyTv.setText("All the fields are required.");
                    msgEmptyTv.setVisibility(View.VISIBLE);
                    notEmpty = false;
                }

                if (usernameOk == true && passwordOk == true && notEmpty == true) {
                    listener.newMemberBasicInfo(name, surname, username, password, dateOfBirth, address);
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RegisterBasicInfoFragment.RegisterBasicInfoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +  "must implement RegisterBasicInfoListener");
        }

    }

    public interface RegisterBasicInfoListener {
        void newMemberBasicInfo(String name, String surname, String username, String password, String dateOfBirth, String address);
    }
}
