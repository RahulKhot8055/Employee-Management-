package com.aditya.employeemanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class AttendanceAutoUncheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Attendance stopped automatically.", Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().child("attendanceStatus")
                .setValue("false");

        // Optionally, you can also trigger an activity-specific action by broadcasting an intent
        Intent broadcastIntent = new Intent("com.aditya.employeemanagement.ATTENDANCE_STOPPED");
        context.sendBroadcast(broadcastIntent);
    }

}
