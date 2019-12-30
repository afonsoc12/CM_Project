package com.cm_project.physio2go.Exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.MainActivity.PlanExerciseListFragment;
import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.R;

public class ExerciseDetailsFragment extends Fragment {

    public static final String EXERCISE_DETAILS_FRAGENT_TAG = "exercise_details_fragment_tag";

    public ExerciseDetailsFragment() {

    }

    public static ExerciseDetailsFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG, exercise);
        ExerciseDetailsFragment fragment = new ExerciseDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_exercise_details, container, false);

        Exercise exercise = (Exercise) getArguments().getSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);

        // Set fragment TextViews
        TextView name = v.findViewById(R.id.exercise_name_tv);
        TextView side = v.findViewById(R.id.exercise_side_tv);
        TextView repetitions = v.findViewById(R.id.exercise_repetitions_tv);
        TextView description = v.findViewById(R.id.exercise_description_tv);
        ImageView exerciseThumbnail = v.findViewById(R.id.exercise_img_iv);

        name.setText(exercise.getName());
        if (exercise.getBody_side() != null) {
            side.setText(String.format("Side: %s", exercise.getBody_side()));
        } else {
            side.setVisibility(View.INVISIBLE);
        }
        repetitions.setText(String.format("Repetitions: %d", exercise.getRepetitions()));
        description.setText(exercise.getDescription());

        // Set image thumbnail
        int exerciseID = exercise.getId();
        switch (exerciseID) {
            case 1:     // Left Arm
            case 2:     // Right Arm (both grouped in case 2)
                exerciseThumbnail.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_arm, null));
                break;
            case 3:     // Left Leg
            case 4:     // Right Leg (both grouped in case 4)
                exerciseThumbnail.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_leg, null));
                break;
            case 5:     // Breathing Exercise
                exerciseThumbnail.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_breath, null));
                break;
            default:
                break;
        }

        return v;
    }
}
