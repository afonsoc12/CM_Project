package com.cm_project.physio2go;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.cm_project.physio2go.AsyncTasks.PlanCompletedAsyncTask;
import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

public class PlanExerciseListFragment extends ListFragment {

    public final static String PLAN_EXERCISE_LIST_FRAGMENT_TAG = "plan_exercise_list_fragment";
    public final static String PLAN_ARG = "planChosen";
    private final static int REQ_DO_EXERCSISE = 1;
    ArrayList<Exercise> exercises;

    // inicializar btn Start
    Button start_btn;

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

        if (plan != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    extractPlanExercisesInfo(plan));
            setListAdapter(arrayAdapter);
        }
        start_btn = v.findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start Exercise Activity
                Intent intentDoExercise = new Intent(getActivity(), DoExerciseActivity.class);
                intentDoExercise.putExtra(PLAN_ARG, plan);
                startActivityForResult(intentDoExercise, REQ_DO_EXERCSISE);
            }
        });
        return v;
    }

    /**
     * Extracts the plan exercise titles, to populate the list adapter
     *
     * @param plan
     * @return
     */
    private ArrayList<String> extractPlanExercisesInfo(Plan plan) {
        ArrayList<String> titles = new ArrayList<>();
        exercises = plan.getExercises();

        for (Exercise thisExercise : exercises) {
            titles.add(thisExercise.getName());
        }

        return titles;
    }




    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Exercise chosenExercise = ((Plan) this.getArguments().getSerializable(PLAN_ARG)).getExercises().get(position);

        // Start Exercise Activity
        Intent intentDoExercise = new Intent(getActivity(), DoExerciseActivity.class);
        intentDoExercise.putExtra(CHOSEN_EXERCISE_ARG, chosenExercise);

        startActivityForResult(intentDoExercise, REQ_DO_EXERCSISE);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_DO_EXERCSISE:
                if (resultCode == Activity.RESULT_OK) { // Exercise finished successfully
                    View view = getActivity().findViewById(R.id.main_activity);
                    Plan plan = (Plan) data.getSerializableExtra(PlanExerciseListFragment.PLAN_ARG);
                    new PlanCompletedAsyncTask(getContext(), view).execute(plan);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Dont do anything, since the exercise was aborted
                    Toast.makeText(getContext(), "You did no succeed finishing the exercise. Try again soon!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "You did no succeed finishing the exercise. Try again soon!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
