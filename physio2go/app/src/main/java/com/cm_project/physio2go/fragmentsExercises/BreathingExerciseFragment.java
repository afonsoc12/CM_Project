package com.cm_project.physio2go.fragmentsExercises;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Exercise;
import com.github.florent37.viewanimator.ViewAnimator;

public class BreathingExerciseFragment extends Fragment {

    private final static String CHOSEN_EXERCISE_ARG = "chosen_ex";

    int reps;
    ImageView imgView;
    Button next_btn;
    TextView finish_tv;
    TextView doneReps_tv;
    int repsDone;
    breathingExerciseListenner breathingExerciseListenner;


    public BreathingExerciseFragment() {
    }

    public static BreathingExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(CHOSEN_EXERCISE_ARG, exercise);

        BreathingExerciseFragment fragment = new BreathingExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_breathe_exercise, container, false);

        next_btn = (Button) getActivity().findViewById(R.id.next_ex_btn);
        next_btn.setVisibility(View.INVISIBLE);

        finish_tv = (TextView) getActivity().findViewById(R.id.finish_ex_tv);
        finish_tv.setVisibility(View.INVISIBLE);

        // Get exercise
        Exercise exercise = (Exercise) getArguments().getSerializable(CHOSEN_EXERCISE_ARG);

        TextView exerciseName_tv = view.findViewById(R.id.exercise_name_tv);
        TextView exerciseSide_tv = view.findViewById(R.id.exercise_side_tv);
        TextView exerciseReps_tv = view.findViewById(R.id.exercise_repetitions_tv);
        ImageView exercise_img = view.findViewById(R.id.exercise_img_iv);

        reps = exercise.getRepetitions();

        exerciseName_tv.setText(exercise.getName());
        exerciseSide_tv.setText(String.format(""));
        exerciseReps_tv.setText(String.format("Repetitions: %d", reps));
        exercise_img.setImageResource(R.drawable.ic_arm);

        TextView totalReps_tv = view.findViewById(R.id.total_reps_tv);
        totalReps_tv.setText(String.format("/%d", reps));

        doneReps_tv = view.findViewById(R.id.reps_done_tv);
        doneReps_tv.setText(Integer.toString(repsDone));

        this.imgView = view.findViewById(R.id.imageView);
        startIntroAnimation();

        if (reps > 0) {
            breatheRoutineAnimation(reps);
            finishExercie(reps);
        }

        return view;
    }

    /**
     * Intro animation routine
     * Source: https://github.com/florent37/ViewAnimator
     */

    public void finishExercie(int nBreath) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish_tv.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        breathingExerciseListenner.finishExercise(true);
                    }
                });
            }
        }, 10100 * nBreath);
    }

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            breathingExerciseListenner = (breathingExerciseListenner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onMessageRead...");
        }
    }

    public interface breathingExerciseListenner {
        public void finishExercise(Boolean message);
    }
}
