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

    private String name;
    private String surname;
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
        fragmentTransaction.replace(R.id.layout, registerBasicInfoFragment, "registerBasicInfoFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void newMemberBasicInfo(String name, String surname, String username, String password, String dateOfBirth, String address) {

        this.name = name;
        this.surname = surname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;

        RegisterHealthInfoFragment registerHealthInfoFragment = RegisterHealthInfoFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout, registerHealthInfoFragment, "registerHealthInfoFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void newMemberHealthInfo(String condition, double height, double weight, Doctor doctor) {

        this.condition = condition;
        this.height = height;
        this.weight = weight;
        this.doctor = doctor;

        newMember = new Patient();
        newMember.setName(name);
        newMember.setSurname(surname);
        newMember.setPassword(password);
        newMember.setDob(dateOfBirth);
        newMember.setAddress(address);
        newMember.setCondition(condition);
        newMember.setHeight(height);
        newMember.setWeight(weight);
        newMember.setDoctor(doctor);

        System.out.println(name + " " + surname + " " + password + " " + dateOfBirth + " " + address + " " + condition + " " + height + " " + weight + " " + doctor.getName());

        server = new ServerDatabaseDriver();
        server.insertNewPatient(newMember);
        setResult(RESULT_OK);
        finish();
    }

}
