package com.cm_project.physio2go.fragmentsExercises;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cm_project.physio2go.PlanExerciseListFragment;
import com.cm_project.physio2go.R;
import com.cm_project.physio2go.classes.Exercise;

public class ArmExerciseFragment extends Fragment implements SensorEventListener {


    int reps;
    String body_side;
    Button next_btn;
    int reps_Done;
    Vibrator v;
    onMessageReadListenner onMessageReadListenner;
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private int count;
    private ProgressBar simpleProgressBar;
    private int progress;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_arm_exercise, container, false);

        next_btn = (Button) v.findViewById(R.id.next_btn);
        next_btn.setVisibility(View.INVISIBLE);

        //Instancia da classe SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Definicao do tipo de sensor que vai ser utilizado
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        Exercise exercise = (Exercise) this.getArguments().getSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);

        // todo just to try
        TextView tv1 = v.findViewById(R.id.tv_exname);
        TextView tv2 = v.findViewById(R.id.tv_bodyside);
        TextView tv3 = v.findViewById(R.id.tv_exdesc);

        reps = exercise.getRepetitions();
        body_side = exercise.getBody_side();

        tv1.setText(exercise.getName());
        tv2.setText(exercise.getBody_side());
        tv3.setText(exercise.getDescription());


        simpleProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        simpleProgressBar.setMax(reps); // get maximum value of the progress bar
        setProgressValue(reps_Done);

        return v;
    }

    public ArmExerciseFragment() {
    }

    public static ArmExerciseFragment newInstance(Exercise exercise) {

        Bundle args = new Bundle();
        args.putSerializable(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG, exercise);

        ArmExerciseFragment fragment = new ArmExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Parameto Sensor_delay_normal define a velocidade da captura das informações
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);

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

        Float x = event.values[0];
        Float y = event.values[1];
        Float z = event.values[2];


        if (count % 2 == 0 && reps > reps_Done) {
            if (body_side.equals("R")) {
                if (x > 8 && y > -2 && y < 2 && z > -2 && z < 2) {

                    // Get instance of Vibrator from current Context
                    v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    count++;
                }
            } else {
                if (x < -8 && y > -2 && y < 2 && z > -2 && z < 2) {
                    // Get instance of Vibrator from current Context
                    v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    count++;
                }
            }
        }

        if (count % 2 != 0 && reps > reps_Done) {
            if (z < -8 && y > -2 && y < 2 && x > -2 && x < 2) {
                v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                count++;
                reps_Done++;
                setProgressValue(reps_Done);
            }
        }

        if (reps == reps_Done && count != 0) {

            Context context = getContext().getApplicationContext();
            CharSequence text = "Exercicio terminado";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            count = 0;
            next_btn.setVisibility(View.VISIBLE);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMessageReadListenner.onMessageRead(true);
                }
            });
        }

    }

    private void setProgressValue(final int progress) {

        // set the progress
        simpleProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public interface onMessageReadListenner {
        public void onMessageRead(Boolean message);
    }
}
