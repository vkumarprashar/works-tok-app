package com.eurasian.workstokapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurasian.workstokapp.R;
import com.eurasian.workstokapp.adapters.MyExpandableListAdapter;
import com.eurasian.workstokapp.adapters.WithdrawAdapter;
import com.eurasian.workstokapp.models.EarningHistory;
import com.eurasian.workstokapp.models.Earnings;
import com.eurasian.workstokapp.models.WorkHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eurasian.workstokapp.ConnectionStrings.URL_PREFIX;

public class WorkHistoryFragment extends Fragment {

    ExpandableListView listView;
    ExpandableListAdapter expandableListAdapter;
    Map<String, List<WorkHistory>> workHistoryCollection;
    DatePickerDialog.OnDateSetListener datePickerStartDate;
    DatePickerDialog.OnDateSetListener datePickerEndDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_history, container, false);
        listView = view.findViewById(R.id.workHistoryListView);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences("EmployeeDetails", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        getWorkHistory(view, id, "07.01.2022", "07.31.2022");

        EditText startDateEditTxt = (EditText) view.findViewById(R.id.startDateEditTxt);
        EditText endDateEditTxt = (EditText) view.findViewById(R.id.endDateEditTxt);

        Button search = view.findViewById(R.id.workHistorySearchBtn);
        search.setOnClickListener(view1 -> {
            String fromDate = startDateEditTxt.getText().toString();
            if (fromDate.isEmpty() || fromDate == null) {
                Toast.makeText(getContext(), "Start Date cannot be empty", Toast.LENGTH_LONG).show();
            }
            String toDate = endDateEditTxt.getText().toString();
            if (toDate.isEmpty() || toDate == null) {
                Toast.makeText(getContext(), "End Date cannot be empty", Toast.LENGTH_LONG).show();
            }
            getWorkHistory(view, id, fromDate, toDate);
        });

        TextView lastWeekTxtView = view.findViewById(R.id.lastWeekTextView);
        TextView lastMonthTxtView = view.findViewById(R.id.lastMonthTxtView);

        lastWeekTxtView.setOnClickListener(view1 ->  {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.DAY_OF_WEEK, -7);
                DateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
                String startDate = "", endDate = "";

                startDate = df.format(calendar.getTime());
                calendar.add(Calendar.DATE, 6);
                endDate = df.format(calendar.getTime());
                startDateEditTxt.setText(startDate);
                endDateEditTxt.setText(endDate);

                lastWeekTxtView.setTextColor(Color.BLUE);
                lastMonthTxtView.setTextColor(Color.BLACK);

                getWorkHistory(view, id, startDate, endDate);
        });

        lastMonthTxtView.setOnClickListener(view1 ->  {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 1);
            DateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
            String startDate = "", endDate = "";

            startDate = df.format(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = df.format(calendar.getTime());

            startDateEditTxt.setText(startDate);
            endDateEditTxt.setText(endDate);
            lastMonthTxtView.setTextColor(Color.BLUE);
            lastWeekTxtView.setTextColor(Color.BLACK);

            getWorkHistory(view, id, startDate, endDate);
        });


        startDateEditTxt.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePickerStartDate, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        datePickerStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                startDateEditTxt.setText(month + "." + day + "." + year);
            }
        };

        endDateEditTxt.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePickerEndDate, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        datePickerEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                endDateEditTxt.setText(month + "." + day + "." + year);
            }
        };


        return view;
    }

    public void getWorkHistory(View view, int employeeId, String fromDate , String toDate) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(URL_PREFIX + "/api/WorkHistory/?FromDate=" + fromDate + "&ToDate=" + toDate)
                .method("GET", null)
                .addHeader("Employee", String.valueOf(employeeId))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("Total Earnings", "onFailure: " + e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    workHistoryCollection = new HashMap<>();
                    List<WorkHistory> list = new ArrayList<>();
                    Log.d("Inside getTotalEarning", "onResponse: " + response.code());

                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        list.add(new WorkHistory(object1.getInt("RequestId"), object1.getString("CompletedWorkTime"), object1.getString("ClientName"),
                                object1.getString("WorkTypeName"), object1.getInt("ExecutionTimeInMinute"), (object1.get("Earning").equals(null)) ? 0 : object1.getDouble("Earning")));
                    }

                    for (int i = 0; i < list.size(); i++) {
                        String workTypeName = list.get(i).getWorkTypeName();
                        Log.d("Work Type Name", "onResponse: " + workTypeName);
                        if (!workHistoryCollection.containsKey(workTypeName)) {
                            workHistoryCollection.put(list.get(i).getWorkTypeName(), Arrays.asList(list.get(i)));
                        } else {
                            List<WorkHistory> wh = new ArrayList<>(workHistoryCollection.get(list.get(i).getWorkTypeName()));
                            wh.add(list.get(i));
                            workHistoryCollection.put(list.get(i).getWorkTypeName(), wh);
                        }
                    }
                    getActivity().runOnUiThread(() -> {

                        expandableListAdapter = new MyExpandableListAdapter(getActivity(), new ArrayList<>(workHistoryCollection.keySet()), workHistoryCollection);
                        listView.setAdapter(expandableListAdapter);

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Json Parse", "onResponse: " + e.getMessage());
                }
            }
        });

    }
}