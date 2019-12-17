package com.cm_project.physio2go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.fragmentsRegister.RegisterBasicInfoFragment;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        RegisterBasicInfoFragment registerBasicInfoFragment = RegisterBasicInfoFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout, registerBasicInfoFragment, "registerBasicInfoFragment");
        fragmentTransaction.commit();
    }

}
