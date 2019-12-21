package com.cm_project.physio2go;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.fragmentsExercises.ArmExerciseFragment;
import com.cm_project.physio2go.fragmentsExercises.BreathingExerciseFragment;
import com.cm_project.physio2go.fragmentsExercises.LegExerciseFragment;

import java.util.ArrayList;

public class DoExerciseActivity extends AppCompatActivity implements ArmExerciseFragment.onMessageReadListenner {

    private final String DIALOG_TAG = "close_dialog";
    ArrayList<Exercise> exercises;
    Boolean exerciseDone = true;
    int positionExercise = 0;
    int numberExercise = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        this.exercises = (ArrayList<Exercise>) getIntent().getSerializableExtra(PlanExerciseListFragment.EXERCISE_LIST_ARG);
        this.numberExercise = this.exercises.size();

        // Inflate toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button actionBtn = findViewById(R.id.next_ex_btn);
        actionBtn.setText("Next");
        showLayouts();

    }

    public void showLayouts() {
        // Instanciate fragment, depending on the exercise
        Fragment exerciseFragment = null;

        if (positionExercise == numberExercise - 1) { // Case last exercise
            Button actionBtn = findViewById(R.id.next_ex_btn);
            actionBtn.setText("Finish");
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
        } else {
            // Set button name
            setResult(RESULT_OK);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main_activity items for use in the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_do_exercise_activity, menu);

        ActionBar ab = getSupportActionBar();

        //ab.setHomeAsUpIndicator(R.drawable.ic_action_app);

        //Show the icon - selecting "home" returns a single level
        //ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Doing an Exercise");

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
            case R.id.quit_exercise_btn: // Confirms quit exerc
                CloseExerciseDialogFragment dialog = new CloseExerciseDialogFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_TAG);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Alert diolog when back button is pressed

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
    public void onMessageRead(Boolean message) {
        positionExercise++;
        showLayouts();
        System.out.println(message);
        System.out.println(positionExercise);
    }
}

