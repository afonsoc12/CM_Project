package com.cm_project.physio2go.Register;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.DatabaseDrivers.ServerDatabaseDriver;
import com.cm_project.physio2go.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterBasicInfoFragment extends Fragment {

    private ServerDatabaseDriver server;
    private RegisterBasicInfoListener listener;

    public RegisterBasicInfoFragment() {
    }

    public static RegisterBasicInfoFragment newInstance() {
        return new RegisterBasicInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        server = new ServerDatabaseDriver(getContext());

        View view = inflater.inflate(R.layout.fragment_register_basicinfo, container, false);


        TextView backToLogin = view.findViewById(R.id.login_btn);
        backToLogin.setOnClickListener(v -> {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        });


        Button next = view.findViewById(R.id.next_btn);
        next.setOnClickListener(v -> {

            TextView msgUsernameTv = view.findViewById(R.id.failed_username_tv);
            TextView msgPasswordTv = view.findViewById(R.id.failed_password_tv);
            TextView msgDateOfBirthTv = view.findViewById(R.id.failed_dob_tv);
            TextView msgEmptyTv = view.findViewById(R.id.failed_empty_tv);
            msgUsernameTv.setVisibility(View.GONE);
            msgPasswordTv.setVisibility(View.GONE);
            msgDateOfBirthTv.setVisibility(View.GONE);
            msgEmptyTv.setVisibility(View.GONE);

            boolean usernameOk = true;
            boolean passwordOk = true;
            boolean dateOfBirthOK = true;
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

            if (usernameExists) {
                msgUsernameTv.setText("This username already exists.");
                msgUsernameTv.setVisibility(View.VISIBLE);
                usernameOk = false;
            }

            // checks if password == confirmPassword
            if (!password.equals(confirmPassword)) {
                msgPasswordTv.setText("Confirm password must match password.");
                msgPasswordTv.setVisibility(View.VISIBLE);
                passwordOk = false;
            }

            // Checks if date of birth is valid
            String dateParsed = null;
            if (!dateOfBirth.isEmpty()) {
                try {
                    dateParsed = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][ddMMyyyy]")).toString();
                } catch (Exception e) {
                    msgDateOfBirthTv.setText("Date is not valid. Use the format dd/mm/yyyy.");
                    msgDateOfBirthTv.setVisibility(View.VISIBLE);
                    dateOfBirthOK = false;
                }
            }

            // checks if all inputs are not empty
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty()) {
                msgEmptyTv.setText("All the fields are required.");
                msgEmptyTv.setVisibility(View.VISIBLE);
                notEmpty = false;
            }

            if (usernameOk && passwordOk && dateOfBirthOK && notEmpty) {
                listener.newMemberBasicInfo(name, surname, username, password, dateParsed, address);
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
            throw new ClassCastException(context.toString() + "must implement RegisterBasicInfoListener");
        }

    }

    public interface RegisterBasicInfoListener {
        void newMemberBasicInfo(String name, String surname, String username, String password, String dateOfBirth, String address);
    }
}
