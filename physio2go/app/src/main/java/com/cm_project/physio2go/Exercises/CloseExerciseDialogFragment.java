package com.cm_project.physio2go.Exercises;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class CloseExerciseDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Quit Exercise")
                .setMessage("Are you sure you want to quit the current exercise session?\nYour progress will not be recorded.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No, I want to recover 😞", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Just to wait for pressing
                        Toast.makeText(getActivity(), "Good!", Toast.LENGTH_LONG).show();
                    }
                });


        return builder.create();
    }
}
