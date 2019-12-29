package com.cm_project.physio2go.Exercises;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.R;

public class LegExerciseFragment extends Fragment implements SensorEventListener {
    private final static String CHOSEN_EXERCISE_ARG = "chosen_ex";

    private int repetitions;
    private Button nextBtn;
    private int repsDone = 0;
    private TextView finishTv;
    private TextView doneRepsTv;
    private Vibrator v;
    private legExerciseListener legExerciseListener;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int count;
    private ProgressBar movingProgressBar;

    public LegExerciseFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            legExerciseListener = (legExerciseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onMessageRead...");
        }
    }

    public static LegExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(CHOSEN_EXERCISE_ARG, exercise);

        LegExerciseFragment fragment = new LegExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_arm_leg_exercise, container, false);

        nextBtn = getActivity().findViewById(R.id.next_ex_btn);
        nextBtn.setVisibility(View.INVISIBLE);

        finishTv = getActivity().findViewById(R.id.finish_ex_tv);
        finishTv.setVisibility(View.INVISIBLE);

        //Instancia da classe SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Definicao do tipo de sensor que vai ser utilizado
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Exercise exercise = (Exercise) this.getArguments().getSerializable(CHOSEN_EXERCISE_ARG);

        TextView exerciseName_tv = view.findViewById(R.id.exercise_name_tv);
        TextView exerciseSide_tv = view.findViewById(R.id.exercise_side_tv);
        TextView exerciseReps_tv = view.findViewById(R.id.exercise_repetitions_tv);
        ImageView exercise_img = view.findViewById(R.id.exercise_img_iv);

        repetitions = exercise.getRepetitions();
        String body_side = exercise.getBody_side();

        exerciseName_tv.setText(exercise.getName());
        exerciseSide_tv.setText(String.format("Side: %s", body_side));
        exerciseReps_tv.setText(String.format("Repetitions: %d", repetitions));
        exercise_img.setImageResource(R.drawable.ic_leg);

        TextView totalReps_tv = view.findViewById(R.id.total_reps_tv);
        totalReps_tv.setText(String.format("/%d", repetitions));

        doneRepsTv = view.findViewById(R.id.reps_done_tv);
        doneRepsTv.setText(String.format("%d", repsDone));

        movingProgressBar = view.findViewById(R.id.doing_ex_pb);
        movingProgressBar.setMax(10); // get maximum value of the progress bar
        setProgressValueMoving(0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Parameto Sensor_delay_normal define a velocidade da captura das informações
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressLint("WrongViewCast")
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (z >= 0 && z <= 10 && y < 0) {
            setProgressValueMoving(z);
        }

        if (count % 2 == 0 && repetitions > repsDone) {
            if (x > -2 && x < 2 && y < -8 && z > -2 && z < 2) {

                // Get instance of Vibrator from current Context
                v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                count++;
            }
        }

        if (count % 2 != 0 && repetitions > repsDone) {
            if (z > 8 && y > -2 && y < 2 && x > -2 && x < 2) {
                v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                count++;
                repsDone++;
                final Handler handler = new Handler();
                handler.postDelayed(() ->
                        v.vibrate(1000), 4000);
                doneRepsTv.setText(String.format("%d", repsDone));
            }
        }

        if (repetitions == repsDone && count != 0) {

            count = 0;
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                finishTv.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                nextBtn.setOnClickListener(v ->
                        legExerciseListener.finishExercise(true));
            }, 4000);
        }
    }

    private void setProgressValueMoving(float z) {
        // set the progress
        int intZ = Math.round(z);
        movingProgressBar.setProgress(intZ);

        // thread is used to change the progress value
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public interface legExerciseListener {
        void finishExercise(Boolean message);
    }
}
