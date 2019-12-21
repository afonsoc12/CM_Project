package com.cm_project.physio2go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.classes.Doctor;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;
import com.cm_project.physio2go.fragmentsRegister.RegisterBasicInfoFragment;
import com.cm_project.physio2go.fragmentsRegister.RegisterHealthInfoFragment;

public class RegisterActivity extends AppCompatActivity implements RegisterBasicInfoFragment.RegisterBasicInfoListener, RegisterHealthInfoFragment.RegisterHealthInfoListener {

    public final static String NEW_PATIENT_ARG = "new_patient_arg";
    private String name;
    private String surname;
    private String username;
    private String password;
    private String dateOfBirth;
    private String address;
    private String condition;
    private double height;
    private double weight;
    private Doctor doctor;
    private Patient newMember;
    private ServerDatabaseDriver server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterBasicInfoFragment registerBasicInfoFragment = RegisterBasicInfoFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.register_activity, registerBasicInfoFragment, "registerBasicInfoFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void newMemberBasicInfo(String name, String surname, String username, String password, String dateOfBirth, String address) {

        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;

        RegisterHealthInfoFragment registerHealthInfoFragment = RegisterHealthInfoFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.register_activity, registerHealthInfoFragment, "registerHealthInfoFragment");
        fragmentTransaction.commit();
    }

    @Override
    public Patient newMemberHealthInfo(String condition, double height, double weight, Doctor doctor) {

        this.condition = condition;
        this.height = height;
        this.weight = weight;
        this.doctor = doctor;

        newMember = new Patient();
        newMember.setName(name);
        newMember.setSurname(surname);
        newMember.setUsername(username);
        newMember.setPassword(password);
        newMember.setDob(dateOfBirth);
        newMember.setAddress(address);
        newMember.setCondition(condition);
        newMember.setHeight(height);
        newMember.setWeight(weight);
        newMember.setDoctor(doctor);

        return newMember;
    }

}
