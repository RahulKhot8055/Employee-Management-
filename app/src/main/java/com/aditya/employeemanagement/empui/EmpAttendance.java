package com.aditya.employeemanagement.empui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.employeemanagement.AttAdapter;
import com.aditya.employeemanagement.AttData;
import com.aditya.employeemanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpAttendance extends AppCompatActivity {

    String empId;
    ProgressDialog progressDialog;
    RecyclerView rcView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_attendance);

        rcView = findViewById(R.id.rcViewAtt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empId = getIntent().getExtras().getString("empId");
        Toast.makeText(this, "Empid: " + empId, Toast.LENGTH_SHORT).show();


        showData();

    }

    void showData(){
        List<AttData> list;
        list = new ArrayList<>();
        final AttAdapter[] attAdapter = new AttAdapter[1];

        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference()
                .child("EmpDetails").child(empId).child("Attendance");
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder attendanceInfo = new StringBuilder();
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    String status = dateSnapshot.getValue(String.class);
                    AttData a = new AttData(date,status);
                    list.add(a);
                }
                // Set the attendance information in the dialog
                rcView.setLayoutManager(new LinearLayoutManager(EmpAttendance.this,LinearLayoutManager.VERTICAL,false));
                attAdapter[0] = new AttAdapter(list);
                rcView.setAdapter(attAdapter[0]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event if needed
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_att, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;

            case R.id.markAtt:
                showMyDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showMyDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking Status...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("attendanceStatus")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String status = snapshot.getValue(String.class);
                        Toast.makeText(EmpAttendance.this, "Attendance Status:" + status, Toast.LENGTH_SHORT).show();

                        Boolean flag = Boolean.parseBoolean(status);
                        if (flag) {
                            Toast.makeText(EmpAttendance.this, "You are allowed to give attendance", Toast.LENGTH_SHORT).show();
                            showAlertDialog();
                        } else {
                            showAttendanceNotLiveDialog();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    void showAttendanceNotLiveDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Attendance Not Live");
        alertDialog.setMessage("Attendance is not live now. You cannot mark your attendance at this moment.");
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    TextView diaText;
    LocalDate curDate;
    String date;

    void showAlertDialog() {
        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.dialog_mark_attendance, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setView(alertCustomDialog);
        final AlertDialog dialog = alertDialog.create();
        Button mark = alertCustomDialog.findViewById(R.id.markAttendance);
        diaText = alertCustomDialog.findViewById(R.id.textView14);
        TextView cancle = alertCustomDialog.findViewById(R.id.cancle);
        dialog.show();

        String str = "Mark your attendance for \n\tDate: ";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            curDate = LocalDate.now();
        }
        date = curDate.toString();
        str += date;
        diaText.setText(str);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToFirebase();
                dialog.hide();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                progressDialog.dismiss();
            }
        });
    }
    void updateToFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference attendanceRef = databaseReference.child("EmpDetails").child(empId).child("Attendance");
        attendanceRef.child(date).setValue("present", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                if (error != null) {
                    Toast.makeText(EmpAttendance.this, "Failed to mark attendance: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EmpAttendance.this, "Your attendance is marked now.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
