package com.cm_project.physio2go.Exercises;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.MainActivity.PlanExerciseListFragment;
import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.Objects.Plan;
import com.cm_project.physio2go.R;

import java.util.ArrayList;

public class DoExerciseActivity extends AppCompatActivity implements ArmExerciseFragment.ArmExerciseListener,
        LegExerciseFragment.legExerciseListener,
        BreathingExerciseFragment.BreathingExerciseListener {

    private final String DIALOG_TAG = "close_dialog";
    private ArrayList<Exercise> exercises;
    private int positionExercise = 0;
    private int numberExercise = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        Plan plan = (Plan) getIntent().getSerializableExtra(PlanExerciseListFragment.PLAN_ARG);
        this.exercises = plan.getExercises();
        this.numberExercise = this.exercises.size();

        // Set Progress bar as GONE
        ProgressBar spinFinish = findViewById(R.id.spin_finish_ex_pb);
        spinFinish.setVisibility(View.GONE);

        // Inflate toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView planName_tv = findViewById(R.id.plan_name_tv);
        planName_tv.setText(plan.getPlan_name());

        Button actionBtn = findViewById(R.id.next_ex_btn);
        actionBtn.setText("Next");

        TextView finish_tv = findViewById(R.id.finish_ex_tv);
        finish_tv.setText("You finished this exercise!");

        showLayouts();
    }

    public void showLayouts() {
        // Instanciate fragment, depending on the exercise
        Fragment exerciseFragment = null;

        if (positionExercise == numberExercise - 1) { // Case last exercise
            Button actionBtn = findViewById(R.id.next_ex_btn);
            actionBtn.setText("Finish");

            TextView finish_tv = findViewById(R.id.finish_ex_tv);
            finish_tv.setText("You finished this plan!");
        }

        if (positionExercise < numberExercise) { // Case not all exercises executed

            Exercise thisExercise = exercises.get(positionExercise);
            int exerciseID = thisExercise.getId();

            switch (exerciseID) {
                case 1:     // Left Arm
                case 2:     // Right Arm (both grouped in case 2)
                    exerciseFragment = ArmExerciseFragment.newInstance(thisExercise);
                    break;

                case 3:     // Left Leg
                case 4:     // Right Leg (both grouped in case 4)
                    exerciseFragment = LegExerciseFragment.newInstance(thisExercise);
                    break;

                case 5:     // Breathing Exercise
                    exerciseFragment = BreathingExerciseFragment.newInstance(thisExercise);
                    break;
                default:
                    break;
            }

            // Instanciate the fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_list_placeholder, exerciseFragment);
            ft.commit();
        } else if (positionExercise == numberExercise) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main_activity items for use in the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_do_exercise_activity, menu);

        ActionBar ab = getSupportActionBar();

        ab.setTitle("Exercise Session");

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles quiting exercise
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.quit_exercise_btn: // Confirms quit exercise
                CloseExerciseDialogFragment dialog = new CloseExerciseDialogFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_TAG);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Alert diaolog when back button is pressed
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        CloseExerciseDialogFragment dialog = new CloseExerciseDialogFragment();
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void finishExercise(Boolean message) {
        positionExercise++;
        showLayouts();
        System.out.println(message);
        System.out.println(positionExercise);
    }
}
