package com.cm_project.physio2go;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

public class PlansListFragment extends ListFragment {

    public static final String PLAN_LIST_FRAGMENT_TAG = "plans_list_fragment";
    private final static String PLANS_ARG = "plans";

    public PlansListFragment() {
    }

    public static PlansListFragment newInstance(ArrayList<Plan> plans) {

        Bundle args = new Bundle();
        args.putSerializable(PLANS_ARG, plans);

        PlansListFragment fragment = new PlansListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_plans, container, false);

        ArrayList<Plan> plans = (ArrayList<Plan>) this.getArguments().getSerializable(PLANS_ARG);

        if (plans != null) {
            if (!plans.isEmpty()) {
                // Hide the No plans message
                getActivity().findViewById(R.id.no_plans_tv).setVisibility(View.GONE);
            }
            setListAdapter(new PlanListAdapter(getActivity(), plans));
        }

        return v;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Plan chosenPlan = ((ArrayList<Plan>) this.getArguments().getSerializable(PLANS_ARG)).get(position);

        Fragment planExercise = PlanExerciseListFragment.newInstance(chosenPlan);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_list_placeholder, planExercise, PlanExerciseListFragment.PLAN_EXERCISE_LIST_FRAGMENT_TAG);
        ft.addToBackStack(PlanExerciseListFragment.PLAN_EXERCISE_LIST_FRAGMENT_TAG);
        ft.commit();
    }

    /**
     * Extracts plan titles, to populate the list adapter
     *
     * @param plans
     * @return
     */
    private ArrayList<String> extractPlansInfo(ArrayList<Plan> plans) {

        ArrayList<String> titles = new ArrayList<>();

        for (Plan thisPlan : plans) {
            titles.add(thisPlan.getPlan_name());
        }

        return titles;
    }
}
