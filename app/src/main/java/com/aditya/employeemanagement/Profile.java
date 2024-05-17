package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
        TextView empName, empDesg, empId, empMob, empEmail, empeducation, empBasicPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        int whiteColor = ContextCompat.getColor(this, android.R.color.white);
        fab.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);

        empName = findViewById(R.id.empName);
        empDesg = findViewById(R.id.designation);
        empId = findViewById(R.id.empId);
        empMob = findViewById(R.id.textView9);
        empEmail = findViewById(R.id.textView11);
        empeducation = findViewById(R.id.textView13);

        empName.setText(getIntent().getStringExtra("empname"));
        empDesg.setText(getIntent().getStringExtra("empDesg"));
        empId.setText(getIntent().getStringExtra("empId"));
        empMob.setText(getIntent().getStringExtra("empMob"));
        empEmail.setText(getIntent().getStringExtra("empMail"));
        empeducation.setText(getIntent().getStringExtra("empEdu"));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}