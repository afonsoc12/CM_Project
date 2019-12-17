package com.cm_project.physio2go.fragmentsExercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.PlanExerciseListFragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Exercise;
import com.github.florent37.viewanimator.ViewAnimator;

public class BreathingExerciseFragment extends Fragment {

    ImageView imgView;

    public BreathingExerciseFragment() {
    }

    public static BreathingExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG, exercise);

        BreathingExerciseFragment fragment = new BreathingExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_breathe_exercise, container, false);

        // Get exercise
        Exercise exercise = (Exercise) getArguments().getSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);

        // Set prescribed number of breathes
        TextView numBreathes = v.findViewById(R.id.n_breathe);
        if (exercise != null) {
            numBreathes.setText("" + exercise.getRepetitions());
        } else {
            numBreathes.setText("0"); // Default is 0
        }

        Button plusBtn = v.findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nreps = getView().findViewById(R.id.n_breathe);
                int nBreths = Integer.parseInt(nreps.getText().toString());
                nBreths++;
                nreps.setText("" + nBreths);
            }
        });

        Button minusBtn = v.findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nreps = getView().findViewById(R.id.n_breathe);
                int nBreths = Integer.parseInt(nreps.getText().toString());
                nBreths--;
                nreps.setText("" + nBreths);
            }
        });

        Button startBtn = v.findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nreps = getView().findViewById(R.id.n_breathe);
                int nBreths = Integer.parseInt(nreps.getText().toString());
                if (nBreths > 0) {
                    breatheRoutineAnimation(nBreths);
                }
            }
        });

        this.imgView = v.findViewById(R.id.imageView);
        startIntroAnimation();

        return v;
    }

    /**
     * Intro animation routine
     * Source: https://github.com/florent37/ViewAnimator
     */
    private void startIntroAnimation() {
        ViewAnimator
                .animate(imgView)
                .translationY(-1000, 0)
                .dp().translationX(-20, 0)
                .decelerate()
                .duration(3000)
                .start();
    }

    /**
     * Breathe routine
     * Source: https://github.com/florent37/ViewAnimator
     *
     * @param nBreaths
     */
    private void breatheRoutineAnimation(int nBreaths) {
        ViewAnimator
                .animate(imgView)
                .scale(1f, 0.2f, 1f)
                .repeatCount(nBreaths - 1)
                .duration(10000) // 5 seconds out, 5 seconds in
                .start();
    }
}
