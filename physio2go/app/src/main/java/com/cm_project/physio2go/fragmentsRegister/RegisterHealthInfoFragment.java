package com.cm_project.physio2go.fragmentsRegister;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Doctor;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

import java.util.ArrayList;

public class RegisterHealthInfoFragment extends Fragment {

    private ServerDatabaseDriver server;
    private RegisterHealthInfoListener listener;

    public RegisterHealthInfoFragment() {

    }

    public static RegisterHealthInfoFragment newInstance() {
        RegisterHealthInfoFragment fragment = new RegisterHealthInfoFragment();
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

        View view = inflater.inflate(R.layout.fragment_register_healthinfo, container, false);

        // populate radio group with the names of the doctors
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.helvetica);
        RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
        ArrayList<Doctor> doctors = server.listOfDoctors();

        for (Doctor doctor : doctors) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(doctor.getName());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            radioButton.setTextColor(getResources().getColor(R.color.gray));
            radioButton.setTypeface(typeface);
            radioGroup.addView(radioButton);
        }

        TextView backToLogin = view.findViewById(R.id.login_btn);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });


        Button add = view.findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                if (notEmpty == true) {

                    height = Double.parseDouble(height_str);
                    weight = Double.parseDouble(weight_str);

                    chosenDoctor = new Doctor();

                    for (Doctor doctor : doctors) {
                        if (doctor.getName().equals(doctorName)) {
                            chosenDoctor = doctor;
                        }
                    }

                    listener.newMemberHealthInfo(condition, height, weight, chosenDoctor);
                }
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
            throw new ClassCastException(context.toString() +  "must implement RegisterHealthInfoListener");
        }

    }

    public interface RegisterHealthInfoListener {
        void newMemberHealthInfo(String condition, double height, double weight, Doctor doctor);
    }

}
