package com.cm_project.physio2go.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.DoctorProfileFragment;
import com.cm_project.physio2go.ExerciseDetailsFragment;
import com.cm_project.physio2go.MainActivity;
import com.cm_project.physio2go.PatientProfileFragment;
import com.cm_project.physio2go.PlanExerciseListFragment;
import com.cm_project.physio2go.PlansListFragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Exercise;
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
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * Deal with the fragment transactions
         */
        LocalDatabase local = new LocalDatabase(context);
        MainActivity mainAct = (MainActivity) context;
        Fragment fragment = null;
        Fragment repeatedFragment;
        FragmentTransaction ft;
        FragmentManager fm = mainAct.getSupportFragmentManager();

        ArrayList<Fragment> fragments = (ArrayList<Fragment>) fm.getFragments();
        String curFragTag = fragments.get(fragments.size() - 1).getTag();

        /* Artifact to deal when the fragment appears even after being popped from backstack.
         * Get the next fragment instead, if there is no TAG. */
        if (curFragTag == null) {
            curFragTag = fragments.get(fragments.size() - 2).getTag();
        }
        switch (curFragTag) {
            case PlansListFragment.PLAN_LIST_FRAGMENT_TAG:
                fragment = PlansListFragment.newInstance(local.getPlansOfUser());
                repeatedFragment = fm.findFragmentByTag(PlansListFragment.PLAN_LIST_FRAGMENT_TAG);
                break;
            case PlanExerciseListFragment.PLAN_EXERCISE_LIST_FRAGMENT_TAG:
                repeatedFragment = fm.findFragmentByTag(PlanExerciseListFragment.PLAN_EXERCISE_LIST_FRAGMENT_TAG);

                // Get previous plan ID
                int prevPlanId = ((Plan) repeatedFragment.getArguments().getSerializable(PlanExerciseListFragment.PLAN_ARG)).getId();

                // Retrieve all plans from local db (already updated and found the plan with the same id) and create the fragment
                ArrayList<Plan> plans = local.getPlansOfUser();
                for (Plan thisPlan : plans) {
                    if (thisPlan.getId() == prevPlanId) {
                        fragment = PlanExerciseListFragment.newInstance(thisPlan);
                        break;
                    }
                }
                break;
            case PatientProfileFragment.PATIENT_PROFILE_FRAGMENT_TAG:
                fragment = PatientProfileFragment.newInstance(local.getPatient());
                repeatedFragment = fm.findFragmentByTag(PatientProfileFragment.PATIENT_PROFILE_FRAGMENT_TAG);
                break;
            case DoctorProfileFragment.DOCTOR_PROFILE_FRAGMENT_TAG:
                fragment = DoctorProfileFragment.newInstance(local.getPatient().getDoctor());
                repeatedFragment = fm.findFragmentByTag(DoctorProfileFragment.DOCTOR_PROFILE_FRAGMENT_TAG);
                break;
            case ExerciseDetailsFragment.EXERCISE_DETAILS_FRAGENT_TAG:
                repeatedFragment = fm.findFragmentByTag(ExerciseDetailsFragment.EXERCISE_DETAILS_FRAGENT_TAG);

                // Get previous exercise ID
                int prevExerciseID = ((Exercise) repeatedFragment.getArguments().getSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG)).getId();

                // Retrieve all plans from local db (already updated and found the plan with the same id) and create the fragment
                plans = local.getPlansOfUser();
                planLoop:
                for (Plan thisPlan : plans) {
                    for (Exercise thisExerciseOfPlan : thisPlan.getExercises()) {
                        if (thisExerciseOfPlan.getId() == prevExerciseID) {
                            fragment = ExerciseDetailsFragment.newInstance(thisExerciseOfPlan);
                            break planLoop;
                        }
                    }
                }
                break;

            default:
                repeatedFragment = null;
                fragment = null;
                break;
        }

        // Start transaction
        ft = fm.beginTransaction();
        if (repeatedFragment != null) {
            ft.remove(repeatedFragment);
            fm.popBackStack();
        }
        ft.replace(R.id.fragment_list_placeholder, fragment, curFragTag);
        ft.addToBackStack(curFragTag);
        ft.commit();

        // Disable ProgressBar
        ProgressBar spinRegister = view.findViewById(R.id.spin_refresh_pb);
        spinRegister.setVisibility(View.GONE);
    }
}


