package com.eurasian.workstokapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.eurasian.workstokapp.fragments.WithdrawFragment;
import com.eurasian.workstokapp.fragments.WorkHistoryFragment;
import com.eurasian.workstokapp.models.TotalEarnings;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.eurasian.workstokapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.eurasian.workstokapp.ConnectionStrings.URL_PREFIX;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;
    private NavigationView navigationView;
    private ApiResponses apiResponses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        //Syncing toggle button with the drawer layout
        toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar, R.string.open, R.string.close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        apiResponses = new ApiResponses();
        //Setting navigation View menu switch
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_work_history: callFragments(new WorkHistoryFragment());
                                                getSupportActionBar().setTitle("Work History");
                                                drawer.close();
                                                break;
                    case R.id.nav_withdraw:  callFragments(new WithdrawFragment());
                                                getSupportActionBar().setTitle("Withdraw");
                                                drawer.close();
                                                break;
                }
                return true;
            }
        });

        //Setting the toolbar title by default
        getSupportActionBar().setTitle("Work History");

        binding.appBarMain.totalEarningsAppBar.setText("");
        callFragments(new WorkHistoryFragment());
        setHeader();
    }

    public void setHeader() {
        View header = navigationView.getHeaderView(0);
        SharedPreferences sp = getSharedPreferences("EmployeeDetails", MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        String fName = sp.getString("fName", null);
        String lName = sp.getString("lName", null);
        String mName = sp.getString("mName", null);
        ((TextView) header.findViewById(R.id.nameInitialTxtView)).setText(fName.charAt(0) + "" + lName.charAt(0));
        ((TextView) header.findViewById(R.id.navBarNameTxtView)).setText(fName + " " + mName + " " + lName);
        ((TextView) header.findViewById(R.id.navBarEmailTxtView)).setText(fName + lName + "@worktoks.com");

        navigationView.setCheckedItem(R.id.nav_work_history);
        getTotalEarnings(id);
    }

    /**
     * Change the fragments in the frame layout
     * @param fragment
     */
    public void callFragments(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram_layout, fragment);
        ft.commit();
    }

    public void getTotalEarnings(int employeeId) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(URL_PREFIX + "/api/TotalEarning")
                .method("GET", null)
                .addHeader("Employee", String.valueOf(employeeId))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("Total Earnings", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    Log.d("Inside getTotalEarning", "onResponse: " + response.code());
                    JSONObject object = new JSONObject(response.body().string());
                    Double total = object.getDouble("TotalEarning");
                    Log.d("Total Earnings", "onResponse: " + total);

                    MainActivity.this.runOnUiThread(() -> {
                        binding.appBarMain.totalEarningsAppBar.setText(String.format("%.0f",total) + " RUB");
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Json Parse", "onResponse: " + e.getMessage());
                }
            }
        });
    }
}