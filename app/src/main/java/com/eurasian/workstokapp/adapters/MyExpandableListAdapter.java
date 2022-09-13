package com.eurasian.workstokapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.eurasian.workstokapp.R;
import com.eurasian.workstokapp.models.WorkHistory;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    Activity activity;
    List<String> groups;
    Map<String, List<WorkHistory>> workHistoryCollection;
    public MyExpandableListAdapter(Activity activity,  List<String> groups, Map<String, List<WorkHistory>> workHistoryCollection) {
        this.activity = activity;
        this.groups = groups;
        this.workHistoryCollection = workHistoryCollection;
    }

    @Override
    public int getGroupCount() {
        return workHistoryCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return workHistoryCollection.get(groups.get(i)).size();
    }

    @Override
    public String getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public WorkHistory getChild(int i, int i1) {
        return workHistoryCollection.get(groups.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String workType = getGroup(i).toString();
        if (view == null) {
            view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.list_view_work_history, null, false);
        }
        ((TextView) view.findViewById(R.id.workHistoryTypeName)).setText(workType);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        WorkHistory workHistory = getChild(i, i1);
        if (view == null) {
            view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.list_view_work_history_nested, null, false);
        }

        ((TextView) view.findViewById(R.id.customerNameTxtView)).setText("#" + workHistory.getRequestId() + " " +workHistory.getClientName());
        ((TextView) view.findViewById(R.id.completedDateTxtView)).setText(workHistory.getCompletedWorkTime()+ " (" + workHistory.getExecutionTimeInMinutes() + " minutes)");
        ((TextView) view.findViewById(R.id.payoutTxtView)).setText("+"+ String.format("%.0f",workHistory.getEarning()) + " RUB");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
