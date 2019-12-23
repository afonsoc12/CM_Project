package com.cm_project.physio2go;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.classes.Doctor;

public class DoctorProfileFragment extends Fragment {
    public static final String DOCTOR_ARG = "patient_tag";
    public static final String DOCTOR_PROFILE_FRAGMENT_TAG = "doctor_profile_fragment_tag";

    public DoctorProfileFragment() {

    }

    public static DoctorProfileFragment newInstance(Doctor doctor) {

        Bundle args = new Bundle();
        args.putSerializable(DOCTOR_ARG, doctor);
        DoctorProfileFragment fragment = new DoctorProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        Doctor doctor = (Doctor) getArguments().getSerializable(DOCTOR_ARG);

        // Set fragment TextViews
        TextView nameTv = v.findViewById(R.id.exercise_name_tv);
        TextView usernameTv = v.findViewById(R.id.exercise_side_tv);
        TextView specialityTv = v.findViewById(R.id.speciality_tv);
        TextView hospitalTv = v.findViewById(R.id.hospital_tv);
        TextView bioTv = v.findViewById(R.id.bio_tv);

        nameTv.setText(String.format("Dr. %s %s", doctor.getName(), doctor.getSurname()));
        usernameTv.setText(doctor.getUsername());
        specialityTv.setText(doctor.getSpeciality());
        hospitalTv.setText(doctor.getHospital());
        bioTv.setText(doctor.getBio());

        return v;
    }
}
