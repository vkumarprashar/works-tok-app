package com.eurasian.workstokapp;

import android.util.Log;

import com.eurasian.workstokapp.models.CompanyCommissions;
import com.eurasian.workstokapp.models.Employee;
import com.eurasian.workstokapp.models.TotalEarnings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.eurasian.workstokapp.ConnectionStrings.URL_PREFIX;

public class ApiResponses {
    List<Employee> employees = new ArrayList<>();
    TotalEarnings totalEarnings;
    /**
     * fetch all employees
     */
    public void getEmployees() {
        employees.clear();
//        employees.add(new Employee(1, "Vinay", "Kumar", "Prashar"));
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(URL_PREFIX + "/api/Employees")
                .method("GET", null)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("Response Error", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        employees.add(new Employee(object.getInt("Id"), object.getString("FirstName"),
                                object.getString("MiddleName"), object.getString("LastName")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Json Parse", "onResponse: " + e.getMessage());
                }
            }
        });
    }

    /**
     * check login
     * @param empId
     * @return
     */
    public Employee login(int empId) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == empId) {
                return employees.get(i);
            }
        }
        return null;
    }

}
