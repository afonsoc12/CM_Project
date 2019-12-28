package com.cm_project.physio2go.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.cm_project.physio2go.DatabaseDrivers.LocalDatabase;
import com.cm_project.physio2go.DatabaseDrivers.ServerDatabaseDriver;
import com.cm_project.physio2go.MainActivity.MainActivity;
import com.cm_project.physio2go.Objects.Plan;

/**
 * This AsyncTask is responsible to save the exercise progress onto the database.
 * If the internet connection is not available, it saves locally, to be synced later on.
 */
public class PlanCompletedAsyncTask extends AsyncTask<Object, Void, Void> {

    private Context context;
    private View view;

    public PlanCompletedAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        Plan plan = (Plan) obj[0];

        boolean hasConnection = MainActivity.isNetworkAvilable(context);

        if (hasConnection) {
            ServerDatabaseDriver db = new ServerDatabaseDriver();
            db.updatePlan(plan);
        } else {
            LocalDatabase local = new LocalDatabase(context);
            local.updatePlanOffline(plan);
            MainActivity.showNoInternetSnackbar(view, "Could not save the exercise on the server. Refresh later.");
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
