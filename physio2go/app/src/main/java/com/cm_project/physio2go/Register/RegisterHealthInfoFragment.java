package com.cm_project.physio2go.Register;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.AsyncTasks.RegisterAsyncTask;
import com.cm_project.physio2go.DatabaseDrivers.ServerDatabaseDriver;
import com.cm_project.physio2go.Objects.Doctor;
import com.cm_project.physio2go.Objects.Patient;
import com.cm_project.physio2go.R;

import java.util.ArrayList;

public class RegisterHealthInfoFragment extends Fragment {

    private ServerDatabaseDriver server;
    private RegisterHealthInfoListener listener;

    public RegisterHealthInfoFragment() {
    }

    public static RegisterHealthInfoFragment newInstance() {
        return new RegisterHealthInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        server = new ServerDatabaseDriver(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_healthinfo, container, false);

        // Set progress bar as GONE
        ProgressBar spinPb = view.findViewById(R.id.spin_register_pb);
        spinPb.setVisibility(View.GONE);

        // populate radio group with the names of the doctors
        RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
        ArrayList<Doctor> doctors = server.listOfDoctors();

        for (Doctor doctor : doctors) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(doctor.getName());
            radioButton.setTextColor(getResources().getColor(R.color.gray));
            radioGroup.addView(radioButton);
        }

        TextView backToLogin = view.findViewById(R.id.login_btn);
        backToLogin.setOnClickListener(v -> {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        });


        Button add = view.findViewById(R.id.add_btn);
        add.setOnClickListener(v -> {

            boolean notEmpty = true;

            String condition = ((EditText) view.findViewById(R.id.condition_et)).getText().toString();
            String height_str = ((EditText) view.findViewById(R.id.height_et)).getText().toString();
            String weight_str = ((EditText) view.findViewById(R.id.weight_et)).getText().toString();

            // get chosen doctor from radiogroup
            int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

            String doctorName = "";

            // If nothing is selected from Radio Group, then it return -1
            if (selectedRadioButtonID != -1) {

                RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonID);
                doctorName = selectedRadioButton.getText().toString();

            }

            // checks if all inputs are not empty
            if (condition.isEmpty() || height_str.isEmpty() || weight_str.isEmpty() || doctorName.isEmpty()) {
                TextView msgEmptyTv = view.findViewById(R.id.failed_empty_tv);
                msgEmptyTv.setText("All the fields are required.");
                msgEmptyTv.setVisibility(View.VISIBLE);
                notEmpty = false;
            }

            double height;
            double weight;
            Doctor chosenDoctor;

            if (notEmpty) {

                height = Double.parseDouble(height_str);
                weight = Double.parseDouble(weight_str);

                chosenDoctor = new Doctor();

                for (Doctor doctor : doctors) {
                    if (doctor.getName().equals(doctorName)) {
                        chosenDoctor = doctor;
                    }
                }

                Patient newPatient = listener.newMemberHealthInfo(condition, height, weight, chosenDoctor);

                View view1 = getActivity().findViewById(R.id.register_activity);
                new RegisterAsyncTask(getActivity(), view1).execute(newPatient);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RegisterHealthInfoFragment.RegisterHealthInfoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RegisterHealthInfoListener");
        }

    }

    public interface RegisterHealthInfoListener {
        Patient newMemberHealthInfo(String condition, double height, double weight, Doctor doctor);
    }
}
