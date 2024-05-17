package com.aditya.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.aditya.employeemanagement.empui.EmpHome;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sh = getSharedPreferences("empData", Context.MODE_PRIVATE);
                String name = sh.getString("enname", "");

                if (name.equals("Admin")) {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    finish(); // Finish current activity to prevent going back to it
                } else if (!name.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, EmpHome.class);
                    intent.putExtra("empId", name);
                    startActivity(intent);
                    finish(); // Finish current activity to prevent going back to it
                } else {
                    startActivity(new Intent(MainActivity.this, LogIn.class));
                    finish(); // Finish current activity to prevent going back to it
                }
            }
        }, 2000);
    }

}