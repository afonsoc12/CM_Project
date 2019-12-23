package com.cm_project.physio2go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

public class PlanListAdapter extends BaseAdapter {

    private ArrayList<Plan> plans;
    private LayoutInflater layoutInflater;

    public PlanListAdapter(Context context, ArrayList<Plan> plan) {
        this.plans = plan;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int position) {
        return plans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.plan_list_item, null);
            holder = new ViewHolder();
            holder.planName = convertView.findViewById(R.id.exercise_name_tv);
            holder.planDates = convertView.findViewById(R.id.exercise_side_tv);
            holder.planProgress = convertView.findViewById(R.id.progress_plan_pb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.planName.setText(plans.get(position).getPlan_name());
        holder.planDates.setText(String.format("From: %s - %s", plans.get(position).getDate_start(),
                plans.get(position).getDate_end()));
        holder.planProgress.setMax(plans.get(position).getTotal_reps());
        holder.planProgress.setProgress(plans.get(position).getReps_done());
        return convertView;
    }

    static class ViewHolder {
        TextView planName;
        TextView planDates;
        ProgressBar planProgress;
    }

}
