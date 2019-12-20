package com.cm_project.physio2go;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.classes.Doctor;
import com.cm_project.physio2go.classes.Patient;

public class ProfileFragment extends Fragment {
    public static String PATIENT_ARG = "patient_tag";
    public static String PROFILE_FRAGMENT_TAG = "profile_fragment_tag";

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(Patient patient) {

        Bundle args = new Bundle();
        args.putSerializable(PATIENT_ARG, patient);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Patient patient = (Patient) getArguments().getSerializable(PATIENT_ARG);
        Doctor doctor = patient.getDoctor();

        // Set fragment TextViews
        TextView nameTv = v.findViewById(R.id.name_tv);
        TextView usernameTv = v.findViewById(R.id.username_tv);
        TextView dobTv = v.findViewById(R.id.dob_tv);
        TextView doctorTv = v.findViewById(R.id.doctor_tv);
        TextView conditionTv = v.findViewById(R.id.condition_tv);
        TextView weightTv = v.findViewById(R.id.weight_tv);
        TextView heightTv = v.findViewById(R.id.height_tv);

        nameTv.setText(String.format("%s %s", patient.getName(), patient.getSurname()));
        usernameTv.setText(patient.getUsername());
        dobTv.setText(patient.getDob());
        doctorTv.setText(String.format("Dr. %s %s", doctor.getName(), doctor.getSurname()));
        conditionTv.setText(patient.getCondition());
        weightTv.setText(String.format("%s kg", patient.getWeight()));
        heightTv.setText(String.format("%s cm", patient.getHeight()));

        return v;
    }
}
