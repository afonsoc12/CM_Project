package com.cm_project.physio2go;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

class PlanExerciseListFragment extends ListFragment {

    private final static String PLAN_ARG = "planChosen";
    private final String CHOSEN_EXERCISE_ARG = "chosenExercise";
    private final int REQ_DO_EXERCSISE = 1;

    public PlanExerciseListFragment() {

    }

    public static PlanExerciseListFragment newInstance(Plan plan) {

        Bundle args = new Bundle();
        args.putSerializable(PLAN_ARG, plan);

        PlanExerciseListFragment fragment = new PlanExerciseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_plan_exercises, container, false);

        Plan plan = (Plan) this.getArguments().getSerializable(PLAN_ARG);

        if (plan != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    extractPlanExercisesInfo(plan));
            setListAdapter(arrayAdapter);
        }

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
        ArrayList<Exercise> exercises = plan.getExercises();

        for (Exercise thisExercise : exercises) {
            titles.add(thisExercise.getName());
        }

        return titles;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Exercise chosenExercise = ((Plan) this.getArguments().getSerializable(PLAN_ARG)).getExercises().get(position);

        // Start Exercise Activity
        Intent intentDoExercise = new Intent(getActivity(), DoExerciseActivity.class);
        intentDoExercise.putExtra(CHOSEN_EXERCISE_ARG, chosenExercise);

        startActivityForResult(intentDoExercise, REQ_DO_EXERCSISE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_DO_EXERCSISE:
                if (resultCode == 1) { // Exercise finished successfully
                    // TODO PERSIST DB THE EXERCISE RESULT
                } else {
                    Toast.makeText(getContext(), "You did no succeed finishing the exercise. Try again soon!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
