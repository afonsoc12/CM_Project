package com.cm_project.physio2go;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    ArrayList<Exercise> exercises;
    Boolean exerciseDone = true;
    int positionExercise = 0;
    int numberExercise = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        // Inflate toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showLayouts();
    }

    public void showLayouts() {
        // Instanciate fragment, depending on the exercise
        Fragment exerciseFragment = null;
        exercises = (ArrayList<Exercise>) getIntent().getSerializableExtra(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);
        numberExercise = exercises.size();

        if (positionExercise < numberExercise) {

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
            case R.id.quit_exercise_btn: // Removes login from sharedprefs and prompts login activity
                //todo prompt DO YOU RLY WANNA QUIT BRO?
                setResult(RESULT_OK);
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageRead(Boolean message) {
        positionExercise++;
        showLayouts();
        System.out.println(message);
        System.out.println(positionExercise);
    }
}
