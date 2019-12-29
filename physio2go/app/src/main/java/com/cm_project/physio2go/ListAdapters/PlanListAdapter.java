package com.cm_project.physio2go.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cm_project.physio2go.Objects.Plan;
import com.cm_project.physio2go.R;

import java.util.ArrayList;

/**
 * Handles the definition of the ListAdapter of each Plan.
 */
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
            holder.planDates = convertView.findViewById(R.id.exercise_date_tv);
            holder.planDescription = convertView.findViewById(R.id.exercise_description_tv);
            holder.planProgress = convertView.findViewById(R.id.progress_plan_pb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.planName.setText(plans.get(position).getPlan_name());

        String startDate = plans.get(position).getDate_start();
        String[] arrayStartDate = startDate.split(" ");
        String endDate = plans.get(position).getDate_end();
        String[] arrayEndDate = endDate.split(" ");
        holder.planDates.setText(String.format("From %s to %s", arrayStartDate[0], arrayEndDate[0]));

        holder.planDescription.setText(plans.get(position).getDescription());
        holder.planProgress.setMax(plans.get(position).getTotal_reps());
        holder.planProgress.setProgress(plans.get(position).getReps_done());
        return convertView;
    }

    static class ViewHolder {
        TextView planName;
        TextView planDates;
        TextView planDescription;
        ProgressBar planProgress;
    }
}
