package com.cm_project.physio2go.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.cm_project.physio2go.MainActivity;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.RegisterActivity;
import com.cm_project.physio2go.classes.Plan;
import com.cm_project.physio2go.databaseDrivers.LocalDatabase;
import com.cm_project.physio2go.databaseDrivers.ServerDatabaseDriver;


public class PlanCompletedAsyncTask extends AsyncTask<Object, Void, Void> {

    private Plan plan;
    private Context context;
    private View view;

    public PlanCompletedAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        this.plan = (Plan) obj[0];

        boolean hasConnection = MainActivity.isNetworkAvilable(context);

        if (hasConnection) {
            ServerDatabaseDriver db = new ServerDatabaseDriver();
            db.updatePlan(plan);
        } else {
            LocalDatabase local = new LocalDatabase(context);
            local.updatePlanOffline(plan);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Enable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_finish_ex_pb);
        spinRegister.setVisibility(View.VISIBLE);
        spinRegister.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RegisterActivity registerActivity = (RegisterActivity) context;
        registerActivity.setResult(Activity.RESULT_OK, registerActivity.getIntent().putExtra(RegisterActivity.NEW_PATIENT_ARG, plan));
        registerActivity.finish();

        // Disable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_finish_ex_pb);
        spinRegister.setVisibility(View.GONE);
    }
}


