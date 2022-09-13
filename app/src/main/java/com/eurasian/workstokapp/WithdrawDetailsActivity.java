package com.eurasian.workstokapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.eurasian.workstokapp.adapters.WithdrawAdapter;
import com.eurasian.workstokapp.databinding.ActivityWithdrawDetailsBinding;
import com.eurasian.workstokapp.models.EarningHistory;
import com.eurasian.workstokapp.models.EarningRequestDetails;
import com.eurasian.workstokapp.models.Earnings;
import com.eurasian.workstokapp.models.WorkHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.eurasian.workstokapp.ConnectionStrings.URL_PREFIX;

public class WithdrawDetailsActivity extends AppCompatActivity {

    ActivityWithdrawDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int requestId = intent.getIntExtra("requestId", -1);

        binding.withdrawDetailsBack.setOnClickListener(view -> {
            finish();
        });
        getRequestDetails(requestId);
    }

    public void getRequestDetails(int requestId) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(URL_PREFIX + "/api/EarningRequestWorkDetail?requestId="+ requestId)
                .method("GET", null)
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

                    EarningRequestDetails earningRequestDetails;
                    Log.d("Inside getTotalEarning", "onResponse: " + response.code());
                    JSONObject object = new JSONObject(response.body().string());
                    earningRequestDetails = new EarningRequestDetails(object.getString("WorkTypeName"),
                            object.getDouble("Esp"), object.getDouble("Wrp"),
                            (object.get("Hbp").equals(null)) ? 0 : object.getDouble("Hbp"),
                            object.getDouble("Scr"), object.getDouble("Ccr"), object.getDouble("QER"));
                    WithdrawDetailsActivity.this.runOnUiThread(() -> {
                        binding.workTypeNameTxtView.setText(earningRequestDetails.getWorkTypeName());
                        binding.baseRateTxtView.setText("" + earningRequestDetails.getWrp().intValue() + " RUB");
                        binding.weekendBonusRate.setText("+" + earningRequestDetails.getHbp().intValue() + " RUB");
                        if (earningRequestDetails.getHbp() > 0 ) {
                            binding.weekendBonusRate.setTextColor(Color.rgb(81, 157, 8));
                        }
                        binding.scrTxtView.setText("-" + earningRequestDetails.getScr().intValue() + " RUB");
                        binding.scrTxtView.setTextColor(Color.RED);
                        binding.ccrTxtView.setText("-" + earningRequestDetails.getCcr().intValue() + " RUB");
                        binding.ccrTxtView.setTextColor(Color.RED);

                        if (earningRequestDetails.getQer() <0) {
                            binding.workQualityRate.setText("-" + earningRequestDetails.getQer().intValue() + " RUB");
                            binding.workQualityRate.setTextColor(Color.RED);
                        } else {
                            binding.workQualityRate.setText("+" + earningRequestDetails.getQer().intValue() + " RUB");
                            binding.workQualityRate.setTextColor(Color.rgb(81, 157, 8));
                        }
                        binding.totalTxtView.setText("" + earningRequestDetails.getEsp().intValue() + " RUB");
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Json Parse", "onResponse: " + e.getMessage());
                }
            }
        });

    }
}