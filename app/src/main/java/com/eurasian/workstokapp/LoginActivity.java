package com.eurasian.workstokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.eurasian.workstokapp.databinding.ActivityLoginBinding;
import com.eurasian.workstokapp.models.Employee;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ApiResponses apiResponses = new ApiResponses();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiResponses.getEmployees();

        binding.loginBtn.setOnClickListener(view -> {
            String text = binding.loginId.getText().toString();
            if (text != null ) {
                Employee employee = apiResponses.login(Integer.valueOf(text));
                if (employee != null) {
                    SharedPreferences sp = getSharedPreferences("EmployeeDetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("id", employee.getId());
                    editor.putString("fName", employee.getFirstName());
                    editor.putString("mName", employee.getMiddleName());
                    editor.putString("lName", employee.getLastName());
                    editor.commit();

                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Login Id not Found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Login Id cannot be null", Toast.LENGTH_SHORT).show();
            }

        });
    }
}