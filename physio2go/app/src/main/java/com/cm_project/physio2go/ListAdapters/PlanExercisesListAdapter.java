package com.cm_project.physio2go.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm_project.physio2go.Objects.Exercise;
import com.cm_project.physio2go.R;

import java.util.ArrayList;

/**
 * Handles the definition of the ListAdapter of the Exercises of each Plan.
 */
public class PlanExercisesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Exercise> exercises;
    private LayoutInflater layoutInflater;

    public PlanExercisesListAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int position) {
        return exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.plan_exercise_list_item, null);
            holder = new ViewHolder();
            holder.exerciseName = convertView.findViewById(R.id.exercise_name_tv);
            holder.exerciseImg = convertView.findViewById(R.id.exercise_img_iv);
            holder.exerciseSide = convertView.findViewById(R.id.exercise_side_tv);
            holder.exerciseRepetitions = convertView.findViewById(R.id.exercise_repetitions_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.exerciseName.setText(exercises.get(position).getName());
        String side = exercises.get(position).getBody_side();
        if (side == null) {
            holder.exerciseSide.setText("");
        } else {
            holder.exerciseSide.setText(String.format("Side: %s", exercises.get(position).getBody_side()));
        }

        holder.exerciseRepetitions.setText(String.format("Repetitions: %d", exercises.get(position).getRepetitions()));

        switch (exercises.get(position).getId()) {
            case 1:     // Left Arm
            case 2:     // Right Arm (both grouped in case 2)
                holder.exerciseImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arm, null));
                break;
            case 3:     // Left Leg
            case 4:     // Right Leg (both grouped in case 4)
                holder.exerciseImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_leg, null));
                break;
            case 5:     // Breathing Exercise
                holder.exerciseImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_breath, null));
                break;
            default:
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView exerciseImg;
        TextView exerciseName;
        TextView exerciseSide;
        TextView exerciseRepetitions;
    }
}
