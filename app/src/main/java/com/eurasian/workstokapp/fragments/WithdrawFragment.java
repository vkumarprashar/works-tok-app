package com.eurasian.workstokapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurasian.workstokapp.MainActivity;
import com.eurasian.workstokapp.R;
import com.eurasian.workstokapp.WithdrawDetailsActivity;
import com.eurasian.workstokapp.adapters.WithdrawAdapter;
import com.eurasian.workstokapp.databinding.FragmentWithdrawBinding;
import com.eurasian.workstokapp.models.EarningHistory;
import com.eurasian.workstokapp.models.EarningRequestDetails;
import com.eurasian.workstokapp.models.Earnings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.eurasian.workstokapp.ConnectionStrings.URL_PREFIX;

public class WithdrawFragment extends Fragment {

    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        listView = view.findViewById(R.id.withDrawListView);
        Button button = view.findViewById(R.id.withdrawBtn);
        SharedPreferences sp = getActivity().getSharedPreferences("EmployeeDetails", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        getTotalEarnings(view, id);

        button.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Withdraw button clicked",Toast.LENGTH_LONG).show();
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earnings earnings = (Earnings) listView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity().getApplicationContext(), WithdrawDetailsActivity.class);
                intent.putExtra("requestId", earnings.getRequestId());
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    public void getTotalEarnings(View view, int employeeId) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(URL_PREFIX + "/api/EarningHistory")
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
                    EarningHistory earningHistory;
                    List<Earnings> list = new ArrayList<>();
                    Log.d("Inside getTotalEarning", "onResponse: " + response.code());
                    JSONObject object = new JSONObject(response.body().string());
                    Double total = object.getDouble("TotalEarning");

                    JSONArray array = object.getJSONArray("EarningHistory");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        list.add(new Earnings(object1.getInt("RequestId"), object1.getInt("RequestRating"),
                                object1.getString("ClientName"), object1.getString("CompletedWorkTime"), object1.getDouble("Earning")));
                    }
                    earningHistory = new EarningHistory(total, list);
                    getActivity().runOnUiThread(() -> {
                        ((TextView) view.findViewById(R.id.availableTxtView)).setText("Available: " + String.format("%.0f",earningHistory.getTotalEarning()) + "RUB");
                        WithdrawAdapter withdrawAdapter = new WithdrawAdapter(getActivity(), list);
                        listView.setAdapter(withdrawAdapter);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Json Parse", "onResponse: " + e.getMessage());
                }
            }
        });

    }
}