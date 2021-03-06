package com.cm_project.physio2go.Exercises;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

public class ArmExerciseFragment extends Fragment implements SensorEventListener {

    private final static String CHOSEN_EXERCISE_ARG = "chosen_ex";

    private int reps;
    private String body_side;
    private Button next_btn;
    private TextView finish_tv;
    private TextView doneReps_tv;
    private int repsDone = 0;
    private ArmExerciseListener armExerciseListener;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int count;
    private ProgressBar movingProgresBar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            armExerciseListener = (ArmExerciseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override onMessageRead...");
        }
    }

    public static ArmExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(CHOSEN_EXERCISE_ARG, exercise);

        ArmExerciseFragment fragment = new ArmExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArmExerciseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_arm_leg_exercise, container, false);

        next_btn = getActivity().findViewById(R.id.next_ex_btn);
        next_btn.setVisibility(View.INVISIBLE);

        finish_tv = getActivity().findViewById(R.id.finish_ex_tv);
        finish_tv.setVisibility(View.INVISIBLE);

        //Instancia da classe SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Definicao do tipo de sensor que vai ser utilizado
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Exercise exercise = (Exercise) this.getArguments().getSerializable(CHOSEN_EXERCISE_ARG);

        TextView exerciseName_tv = view.findViewById(R.id.exercise_name_tv);
        TextView exerciseSide_tv = view.findViewById(R.id.exercise_side_tv);
        TextView exerciseReps_tv = view.findViewById(R.id.exercise_repetitions_tv);
        ImageView exercise_img = view.findViewById(R.id.exercise_img_iv);

        reps = exercise.getRepetitions();
        body_side = exercise.getBody_side();

        exerciseName_tv.setText(exercise.getName());
        exerciseSide_tv.setText(String.format("Side: %s", body_side));
        exerciseReps_tv.setText(String.format("Repetitions: %d", reps));
        exercise_img.setImageResource(R.drawable.ic_arm);

        TextView totalReps_tv = view.findViewById(R.id.total_reps_tv);
        totalReps_tv.setText(String.format("/%d", reps));

        doneReps_tv = view.findViewById(R.id.reps_done_tv);
        doneReps_tv.setText(Integer.toString(repsDone));

        movingProgresBar = view.findViewById(R.id.doing_ex_pb);
        movingProgresBar.setMax(10); // get maximum value of the progress bar
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

        if (body_side.equals("R")) {
            if (z >= 0 && z <= 10 && x < 0) {
                setProgressValueMoving(z);
            }
        } else {
            if (z >= 0 && z <= 10 && x > 0) {
                setProgressValueMoving(z);
            }
        }

        Vibrator v1;
        if (count % 2 == 0 && reps > repsDone) {
            if (body_side.equals("R")) {

                if (x < -8 && y > -2 && y < 2 && z > -2 && z < 2) {

                    // Get instance of Vibrator from current Context
                    v1 = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(300);
                    count++;
                }
            } else {
                if (x > 8 && y > -2 && y < 2 && z > -2 && z < 2) {
                    // Get instance of Vibrator from current Context
                    v1 = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(300);
                    count++;
                }
            }
        }

        if (count % 2 != 0 && reps > repsDone) {
            if (z > 8 && y > -2 && y < 2 && x > -2 && x < 2) {
                v1 = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v1.vibrate(300);
                count++;
                repsDone++;
                doneReps_tv.setText(Integer.toString(repsDone));
            }
        }


        if (reps == repsDone && count != 0) {

            count = 0;
            finish_tv.setVisibility(View.VISIBLE);
            next_btn.setVisibility(View.VISIBLE);
            next_btn.setOnClickListener(v ->
                    armExerciseListener.finishExercise(true));
        }

    }

    private void setProgressValueMoving(float z) {
        // set the progress
        int intZ = Math.round(z);
        movingProgresBar.setProgress(intZ);

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

    public interface ArmExerciseListener {
        void finishExercise(Boolean message);
    }
}
