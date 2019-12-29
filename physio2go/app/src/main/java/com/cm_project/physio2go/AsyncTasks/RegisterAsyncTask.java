package com.cm_project.physio2go.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.cm_project.physio2go.DatabaseDrivers.ServerDatabaseDriver;
import com.cm_project.physio2go.Objects.Patient;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.Register.RegisterActivity;

/**
 * AsyncTask that handles the signup operations
 */
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

        ServerDatabaseDriver db = new ServerDatabaseDriver(context);
        db.insertNewPatient(patient);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Enable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_register_pb);
        spinRegister.setVisibility(View.VISIBLE);
        spinRegister.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RegisterActivity registerActivity = (RegisterActivity) context;
        registerActivity.setResult(Activity.RESULT_OK, registerActivity.getIntent().putExtra(RegisterActivity.NEW_PATIENT_ARG, patient));
        registerActivity.finish();

        // Disable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_register_pb);
        spinRegister.setVisibility(View.GONE);
    }
}
