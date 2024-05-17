package com.aditya.employeemanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.employeemanagement.AttEmpAdapter;
import com.aditya.employeemanagement.AttendanceAutoUncheckReceiver;
import com.aditya.employeemanagement.R;
import com.aditya.employeemanagement.SimpleEmpData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Attendance extends AppCompatActivity {

    RecyclerView rcView;
    AttEmpAdapter attEmpAdapter;
    DatabaseReference attendanceStatusRef;
    MenuItem startAttendanceMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rcView = findViewById(R.id.employeesView2);
        attendanceStatusRef = FirebaseDatabase.getInstance().getReference().child("attendanceStatus");
        displayEmps();
    }

    void displayEmps() {
        List<SimpleEmpData> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("EmpDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String empName = dataSnapshot.child("strEmpName").getValue(String.class);
                            String desg = dataSnapshot.child("strEmpDesg").getValue(String.class);
                            String empId = dataSnapshot.child("strEmpId").getValue(String.class);
                            String mob = dataSnapshot.child("strEmpMob").getValue(String.class);
                            String mail = dataSnapshot.child("strEmpMail").getValue(String.class);
                            String edu = dataSnapshot.child("strEmpEdu").getValue(String.class);
                            String basePay = dataSnapshot.child("strEmpBasicPay").getValue(String.class);

                            if (rcView != null) {
                                list.add(new SimpleEmpData(empName, desg, empId, mob, mail, edu, basePay));
                                rcView.setLayoutManager(new LinearLayoutManager(Attendance.this, LinearLayoutManager.VERTICAL, false));
                                attEmpAdapter = new AttEmpAdapter(list);
                                rcView.setAdapter(attEmpAdapter);
                            } else {
                                Log.e("DisplayEmps", "RecyclerView is null");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance, menu);
        startAttendanceMenuItem = menu.findItem(R.id.startAttendance);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startAttendance:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    item.setTitle("Stop Attendance");
                    changeValue("true");
                    scheduleAutoUncheckTask();
                } else {
                    item.setTitle("Start Attendance");
                    changeValue("false");
                    cancelAutoUncheckTask();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void changeValue(String str){
        attendanceStatusRef.setValue(str);
    }

    void scheduleAutoUncheckTask() {
        // Create an intent to be fired after 5 minutes
        Intent intent = new Intent(this, AttendanceAutoUncheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); // Use FLAG_IMMUTABLE

        // Set an alarm to trigger the intent after 5 minutes
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long currentTime = System.currentTimeMillis();
            long fiveMinutesInMillis = 1 * 60 * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + fiveMinutesInMillis, pendingIntent);
        }
    }
    void cancelAutoUncheckTask() {
        // Cancel the scheduled alarm by creating a pending intent with the same intent and canceling it
        Intent intent = new Intent(this, AttendanceAutoUncheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
