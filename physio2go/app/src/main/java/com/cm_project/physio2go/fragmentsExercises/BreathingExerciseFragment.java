package com.cm_project.physio2go.fragmentsExercises;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.PlanExerciseListFragment;
import com.cm_project.physio2go.classes.Exercise;

public class BreathingExerciseFragment extends Fragment {

    public BreathingExerciseFragment() {
    }

    public static BreathingExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG, exercise);

        BreathingExerciseFragment fragment = new BreathingExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
