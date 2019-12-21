package com.cm_project.physio2go.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.cm_project.physio2go.RegisterActivity;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;


public class RegisterAsyncTask extends AsyncTask<Object, Void, Void> {

    private Patient patient;
    private Context context;
    private View view;

    public RegisterAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        this.patient = (Patient) obj[0];

        ServerDatabaseDriver db = new ServerDatabaseDriver();
        db.insertNewPatient(patient);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Enable ProgressBar
        // ProgressBar spinLogin = view.findViewById(R.id.spin_login_pb);
        //spinLogin.setVisibility(View.VISIBLE);
        //spinLogin.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RegisterActivity registerActivity = (RegisterActivity) context;
        registerActivity.setResult(Activity.RESULT_OK, registerActivity.getIntent().putExtra(RegisterActivity.NEW_PATIENT_ARG, patient));
        registerActivity.finish();
        //  LoginActivity loginActivity = (LoginActivity) context;
        //  View actView = view.findViewById(R.id.login_activity);

        // Disable ProgressBar
        //ProgressBar spinLogin = view.findViewById(R.id.spin_login_pb);
        ///spinLogin.setVisibility(View.GONE);
    }
}


