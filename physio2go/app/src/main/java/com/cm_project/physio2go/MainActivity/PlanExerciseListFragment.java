package com.cm_project.physio2go.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.cm_project.physio2go.AsyncTasks.PlanCompletedAsyncTask;
import com.cm_project.physio2go.Exercises.DoExerciseActivity;
import com.cm_project.physio2go.Exercises.ExerciseDetailsFragment;
import com.cm_project.physio2go.ListAdapters.PlanExercisesListAdapter;
import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.Objects.Plan;
import com.cm_project.physio2go.R;

import java.util.ArrayList;

public class PlanExerciseListFragment extends ListFragment {

    public final static String PLAN_EXERCISE_LIST_FRAGMENT_TAG = "plan_exercise_list_fragment";
    public final static String PLAN_ARG = "planChosen";
    public final static String CHOSEN_EXERCISE_ARG = "exerciseToView";
    private final static int REQ_DO_EXERCISE = 1;

    public PlanExerciseListFragment() {
    }

    public static PlanExerciseListFragment newInstance(Plan plan) {

        Bundle args = new Bundle();
        args.putSerializable(PLAN_ARG, plan);

        PlanExerciseListFragment fragment = new PlanExerciseListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_plan_exercises, container, false);

        Plan plan = (Plan) this.getArguments().getSerializable(PLAN_ARG);
        ArrayList<Exercise> exercises = plan.getExercises();
        TextView noExercisesTv = getActivity().findViewById(R.id.no_plans_tv);
        if (exercises != null) {
            if (!exercises.isEmpty()) {
                // Hide the No plans message
                noExercisesTv.setVisibility(View.GONE);
            } else {
                // Show the No plans message and hide start button
                noExercisesTv.setText("This plan does not have any exercises.");
                noExercisesTv.setVisibility(View.VISIBLE);
                v.findViewById(R.id.start_btn).setVisibility(View.GONE);
            }
            setListAdapter(new PlanExercisesListAdapter(getActivity(), exercises));
        }

        Button startBtn = v.findViewById(R.id.start_btn);
        startBtn.setOnClickListener(v1 -> {

            // Start Exercise Activity
            Intent intentDoExercise = new Intent(getActivity(), DoExerciseActivity.class);
            intentDoExercise.putExtra(PLAN_ARG, plan);
            startActivityForResult(intentDoExercise, REQ_DO_EXERCISE);
        });
        return v;
    }

    /**
     * Handles exercise click to view its properties and description.
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Exercise chosenExercise = ((Plan) this.getArguments().getSerializable(PLAN_ARG)).getExercises().get(position);

        // Start Fragment
        Fragment exerciseFragment = ExerciseDetailsFragment.newInstance(chosenExercise);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_list_placeholder, exerciseFragment, ExerciseDetailsFragment.EXERCISE_DETAILS_FRAGENT_TAG);
        ft.addToBackStack(ExerciseDetailsFragment.EXERCISE_DETAILS_FRAGENT_TAG);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_DO_EXERCISE:
                if (resultCode == Activity.RESULT_OK) { // Exercise finished successfully
                    View view = getActivity().findViewById(R.id.main_activity);
                    Plan plan = (Plan) data.getSerializableExtra(PlanExerciseListFragment.PLAN_ARG);
                    new PlanCompletedAsyncTask(getContext(), view).execute(plan);
                    Toast.makeText(getContext(), "The current plan session has been finished!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Dont do anything, since the exercise was aborted
                    Toast.makeText(getContext(), "You did no succeed finishing the exercise. Try again soon!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
