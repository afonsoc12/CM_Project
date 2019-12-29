package com.cm_project.physio2go.Exercises;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.R;
import com.github.florent37.viewanimator.ViewAnimator;

public class BreathingExerciseFragment extends Fragment {

    private final static String CHOSEN_EXERCISE_ARG = "chosen_ex";

    private ImageView imgView;
    private Button nextBtn;
    private TextView finishTv;
    private BreathingExerciseListener breathingExerciseListener;

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

        nextBtn = getActivity().findViewById(R.id.next_ex_btn);
        nextBtn.setVisibility(View.GONE);

        finishTv = getActivity().findViewById(R.id.finish_ex_tv);
        finishTv.setVisibility(View.GONE);

        // Get exercise
        Exercise exercise = (Exercise) getArguments().getSerializable(CHOSEN_EXERCISE_ARG);

        TextView exerciseName_tv = view.findViewById(R.id.exercise_name_tv);
        TextView exerciseSide_tv = view.findViewById(R.id.exercise_side_tv);
        TextView exerciseRepsTv = view.findViewById(R.id.exercise_repetitions_tv);
        ImageView exercise_img = view.findViewById(R.id.exercise_img_iv);

        int reps = exercise.getRepetitions();

        exerciseName_tv.setText(exercise.getName());
        exerciseSide_tv.setText("");
        exerciseRepsTv.setText(String.format("Repetitions: %d", reps));
        exercise_img.setImageResource(R.drawable.ic_breath);


        // Set prescribed number of breathes
        TextView numBreathes = view.findViewById(R.id.n_breathe);
        numBreathes.setText(String.format("%d", reps));

        Button plusBtn = view.findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(v -> {
            TextView nreps = getView().findViewById(R.id.n_breathe);
            int nBreths = Integer.parseInt(nreps.getText().toString());
            nBreths++;
            nreps.setText(String.format("%d", nBreths));
        });

        Button minusBtn = view.findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(v -> {
            TextView nreps = getView().findViewById(R.id.n_breathe);
            int nBreths = Integer.parseInt(nreps.getText().toString());
            nBreths--;
            if (nBreths >= 1) {
                nreps.setText(String.format("%d", nBreths));
            } else {
                nBreths = 1;
                nreps.setText(String.format("%d", nBreths));
            }
        });

        Button startBtn = view.findViewById(R.id.start_btn);
        startBtn.setOnClickListener(v -> {
            TextView nreps = getView().findViewById(R.id.n_breathe);
            int nBreths = Integer.parseInt(nreps.getText().toString());

            exerciseRepsTv.setText(String.format("Repetitions: %d", nBreths));

            if (nBreths > 0) {
                breatheRoutineAnimation(nBreths);
            }

            numBreathes.setVisibility(View.INVISIBLE);
            plusBtn.setVisibility(View.INVISIBLE);
            minusBtn.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.INVISIBLE);

            exerciseRepsTv.setVisibility(View.VISIBLE);
        });

        this.imgView = view.findViewById(R.id.imageView);
        this.imgView.setVisibility(View.VISIBLE);

        startIntroAnimation();

        return view;
    }

    private void finishExercise() {
        finishTv.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(v ->
                breathingExerciseListener.finishExercise(true));
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
                .onStop(this::finishExercise)
                .start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            breathingExerciseListener = (BreathingExerciseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onMessageRead...");
        }
    }

    public interface BreathingExerciseListener {
        void finishExercise(Boolean message);
    }
}
