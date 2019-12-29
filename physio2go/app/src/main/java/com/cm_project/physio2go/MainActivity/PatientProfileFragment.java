package com.cm_project.physio2go.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.Objects.Doctor;
import com.cm_project.physio2go.Objects.Patient;
import com.cm_project.physio2go.R;

public class PatientProfileFragment extends Fragment {

    public static final String PATIENT_ARG = "patient_tag";
    public static final String PATIENT_PROFILE_FRAGMENT_TAG = "patient_profile_fragment_tag";

    public PatientProfileFragment() {
    }

    public static PatientProfileFragment newInstance(Patient patient) {

        Bundle args = new Bundle();
        args.putSerializable(PATIENT_ARG, patient);
        PatientProfileFragment fragment = new PatientProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        // Hide the No plans message
        getActivity().findViewById(R.id.no_plans_tv).setVisibility(View.GONE);

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

        String dob = patient.getDob();
        String[] arrayDob = dob.split(" ");
        dobTv.setText(arrayDob[0]);

        doctorTv.setText(String.format("Dr. %s %s", doctor.getName(), doctor.getSurname()));
        conditionTv.setText(patient.getCondition());
        weightTv.setText(String.format("%s kg", patient.getWeight()));
        heightTv.setText(String.format("%s cm", patient.getHeight()));

        return v;
    }
}
