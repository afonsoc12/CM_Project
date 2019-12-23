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
    ImageView imgView;
    Button next_btn;
    onMessageReadListenner onMessageReadListenner;


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
        View v = inflater.inflate(R.layout.fragment_breathe_exercise, container, false);

        next_btn = (Button) getActivity().findViewById(R.id.next_ex_btn);
        next_btn.setVisibility(View.INVISIBLE);

        // Get exercise
        Exercise exercise = (Exercise) getArguments().getSerializable(CHOSEN_EXERCISE_ARG);

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
                    startBtn.setVisibility(View.GONE);
                    breatheRoutineAnimation(nBreths);
                    finishExercie(nBreths);
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

    public void finishExercie(int nBreath) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = getContext().getApplicationContext();
                CharSequence text = "Exercicio terminado";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                next_btn.setVisibility(View.VISIBLE);
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onMessageReadListenner.onMessageRead(true);
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
            onMessageReadListenner = (onMessageReadListenner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onMessageRead...");
        }
    }

    public interface onMessageReadListenner {
        public void onMessageRead(Boolean message);
    }
}
