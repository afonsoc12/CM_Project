package com.cm_project.physio2go.fragmentsExercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.PlanExerciseListFragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Exercise;

public class ArmExerciseFragment extends Fragment {

    public ArmExerciseFragment() {
    }

    public static ArmExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG, exercise);

        ArmExerciseFragment fragment = new ArmExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_arm_exercise, container, false);

        Exercise exercise = (Exercise) this.getArguments().getSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);

        // todo just to try
        TextView tv1 = v.findViewById(R.id.tv_exname);
        TextView tv2 = v.findViewById(R.id.tv_bodyside);
        TextView tv3 = v.findViewById(R.id.tv_exdesc);

        tv1.setText(exercise.getName());
        tv2.setText(exercise.getBody_side());
        tv3.setText(exercise.getDescription());

        return v;
    }
}
