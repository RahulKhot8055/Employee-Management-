package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
public class AddAnnouncement extends AppCompatActivity {
    Button save;
//    Context context = this;
    AlertDialog dialog;
    CheckBox chbx;
    Button send;
    TextInputEditText msgText;
    TextView dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        save = findViewById(R.id.sendAnc);
        msgText = findViewById(R.id.smsText);
//        chbx = findViewById(R.id.selectAll);

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.dialog_select_emp,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setView(alertCustomDialog);
//        Button btn = alertCustomDialog.findViewById(R.id.sendsms);
        final AlertDialog dialog = alertDialog.create();

        RecyclerView rcView = alertCustomDialog.findViewById(R.id.selectEmpRcview);
        CheckBox selectAll;
        selectAll = alertCustomDialog.findViewById(R.id.selectAll);
        send = alertCustomDialog.findViewById(R.id.sendsms);
        dismiss = alertCustomDialog.findViewById(R.id.dismiss);
        final SendSmsEmpAdapter[] empAdapter = new SendSmsEmpAdapter[1];

        List<SimpleEmpData> list;
        list = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("EmpDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String empName = dataSnapshot.child("strEmpName").getValue(String.class);
                            String desg = dataSnapshot.child("strEmpDesg").getValue(String.class);
                            String empId = dataSnapshot.child("strEmpId").getValue(String.class);
                            String mobile = dataSnapshot.child("strEmpMob").getValue(String.class);

//                                Toast.makeText(ManageEmployee.this, "Name: " + empName + " desg: " + desg, Toast.LENGTH_SHORT).show();
//                            list.add(new SimpleEmpData(empName, desg,empId,mobile, ));
                        }

                        // Set RecyclerView adapter here after adding all data
                        rcView.setLayoutManager(new LinearLayoutManager(AddAnnouncement.this, LinearLayoutManager.VERTICAL,false));
                        empAdapter[0] = new SendSmsEmpAdapter(list);
                        rcView.setAdapter(empAdapter[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    empAdapter[0].checkAll();
                }else{
                    empAdapter[0].unCheckAll();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] checkedItems = empAdapter[0].getCheckedItems();
                List<SimpleEmpData> selectedItems = new ArrayList<>();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedItems.add(empAdapter[0].empList.get(i));
                    }
                }

                StringBuilder namesBuilder = new StringBuilder();
                for (SimpleEmpData item : selectedItems) {
                    namesBuilder.append(item.getEmpMob()).append(" , ");
                }

                String numbers = namesBuilder.toString().trim();
                String sms = msgText.getText().toString().trim();

                if(sms.isEmpty()){
                    Toast.makeText(AddAnnouncement.this, "Please enter any message to send.", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendSMS(numbers, sms);
            }
        });
        findViewById(R.id.sendAnc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();
                    }
                });

       dismiss.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });
    }
            @Override
            public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        // Handle the back button click
                        onBackPressed();
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }

    public void sendSMS(String numbersList, String sms) {
        // Check if the app has permission to send SMS

            // Permission is already granted, proceed with sending SMS
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String[] numbersArray = numbersList.split(","); // Split the numbers by comma
            for (String number : numbersArray) {
                smsManager.sendTextMessage(number, null, sms, null, null);
            }
            Toast.makeText(this, "Announcement sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        }
        }