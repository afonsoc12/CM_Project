package com.cm_project.physio2go.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.classes.Plan;
import com.cm_project.physio2go.databaseDrivers.LocalDatabase;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class RefreshAsyncTask extends AsyncTask<Object, Void, Void> {

    private Context context;
    private View view;

    public RefreshAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        Log.v("~~~~~~~~~~~~", "doInBackground");
        Patient patient = (Patient) obj[0];
        LocalDatabase local = new LocalDatabase(context);
        ArrayList<Integer> planIDs = local.getOfflinePlans();

        ServerDatabaseDriver server = new ServerDatabaseDriver();

        if (planIDs != null && planIDs.size() > 0) {
            server.incrementOfflinePlans(planIDs);  // Increment on server
            local.deleteTmpPlanIncrements();        // Delete temporary increments
        }

        // Retrieve updated data from server
        ArrayList<Plan> plansFromServer = server.getPlansOfUser(patient.getUsername(), "not async");
        patient = server.getPatientDetails(patient.getUsername(), "not async");

        // Delete files from localDatabase
        local.delete();
        Log.v("~~~~~~~~~~~~", "SAIU doInBackground");
        // Updates plans and exercises
        local.updatePlans(plansFromServer);
        local.updatePatientDetails(patient);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Enable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_refresh_pb);
        spinRegister.setVisibility(View.VISIBLE);
        spinRegister.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        /*
         * The doOnBackground operation is quite fast. For UX reasons, apply a small delay, to force
         * the visualisation of the Progressbar.
         */
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Disable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_refresh_pb);
        spinRegister.setVisibility(View.GONE);
    }
}


