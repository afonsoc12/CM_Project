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

public class DoExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        // Inflate toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instanciate fragment, depending on the exercise
        Fragment exerciseFragment = null;
        Exercise exercise = (Exercise) getIntent().getSerializableExtra(PlanExerciseListFragment.CHOSEN_EXERCISE_ARG);
        int exerciseID = exercise.getId();

        switch (exerciseID) {
            case 1:     // Left Arm
            case 2:     // Right Arm (both grouped in case 2)
                exerciseFragment = ArmExerciseFragment.newInstance(exercise);
                break;

            case 3:     // Left Leg
            case 4:     // Right Leg (both grouped in case 4)
                exerciseFragment = LegExerciseFragment.newInstance(exercise);
                break;

            case 5:     // Breathing Exercise
                exerciseFragment = BreathingExerciseFragment.newInstance(exercise);
                break;
            default:
                break;
        }

        // Instanciate the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_list_placeholder, exerciseFragment);
        ft.commit();
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
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

