package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployee extends AppCompatActivity {

    TextInputEditText empId, empName, empDesg, empMob, empEdu, empBasePay, empMail;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StoreEmpl emp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empId = findViewById(R.id.empId);
        empName = findViewById(R.id.empName);
        empDesg = findViewById(R.id.designation);
        empMob = findViewById(R.id.mobileNo);
        empEdu = findViewById(R.id.education);
        empBasePay = findViewById(R.id.basicPay);
        save = findViewById(R.id.save);
        empMail = findViewById(R.id.emailId);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("EmpDetails");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmpId, strEmpName, strEmpDesg, strEmpMob, strEmpEdu ;
                String strEmpBasicPay, strEmpMail;

                strEmpId = empId.getText().toString().trim();
                strEmpName = empName.getText().toString().trim();
                strEmpDesg = empDesg.getText().toString().trim();
                strEmpMob = empMob.getText().toString().trim();
                strEmpEdu = empEdu.getText().toString().trim();
                strEmpBasicPay = empBasePay.getText().toString().trim();
                strEmpMail = empMail.getText().toString().trim();

                if(strEmpId.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee id should not be empty", Toast.LENGTH_SHORT).show();
                    empId.requestFocus();
                    return;
                }else{
                    strEmpId = "ARSL"+strEmpId;
                }

                if(strEmpName.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee name should not be empty", Toast.LENGTH_SHORT).show();
                    empName.requestFocus();
                    return;
                }else if(!(isFullname(strEmpName))){
                    Toast.makeText(AddEmployee.this, "Enter full name of employee", Toast.LENGTH_SHORT).show();
                    empName.requestFocus();
                    return;
                }


                if(strEmpDesg.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee designation should not be empty", Toast.LENGTH_SHORT).show();
                    empDesg.requestFocus();
                    return;
                }

                if(strEmpMob.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee mobile should not be empty", Toast.LENGTH_SHORT).show();
                    empMob.requestFocus();
                    return;
                }else if(!(isValidPhoneNumber(strEmpMob))){
                    Toast.makeText(AddEmployee.this, "Incorrect mobile number entered.", Toast.LENGTH_SHORT).show();
                    empMob.requestFocus();
                    return;
                }

                if(strEmpMail.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee mail id should not be empty", Toast.LENGTH_SHORT).show();
                    empMail.requestFocus();
                    return;
                } else if (!(isValidEmail(strEmpMail))) {
                    Toast.makeText(AddEmployee.this, "Enter correct mail id", Toast.LENGTH_SHORT).show();
                    empMail.requestFocus();
                    return;
                }

                if(strEmpEdu.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Employee education should not be empty", Toast.LENGTH_SHORT).show();
                    empEdu.requestFocus();
                    return;
                }

                if(strEmpBasicPay.isEmpty()){
                    Toast.makeText(AddEmployee.this, "Basic pay should not be empty", Toast.LENGTH_SHORT).show();
                    empId.requestFocus();
                    return;
                }

                addDataToFirebase(strEmpId, strEmpName, strEmpDesg, strEmpMob,strEmpMail,strEmpEdu,strEmpBasicPay);

            }
        });
    }

    public static boolean isFullname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "(0/91)?[7-9][0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
    void addDataToFirebase(String strEmpId, String strEmpName, String strEmpDesg, String strEmpMob, String strEmpMail, String strEmpEdu, String strEmpBasicPay) {

        String[] arr = strEmpName.split(" ");
        String name = arr[0];


        String strPass = strEmpId+name;

        FirebaseDatabase.getInstance().getReference().child("EmpDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean employeeExists = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String existingEmpId = dataSnapshot.child("strEmpId").getValue(String.class);
                            if (existingEmpId != null && existingEmpId.equals(strEmpId)) {
                                Toast.makeText(AddEmployee.this, "Employee with this id exists already.", Toast.LENGTH_SHORT).show();
                                empId.requestFocus();
                                employeeExists = true;
                                break;
                            }
                        }

                        if (!employeeExists) {
                            // Employee doesn't exist, add to Firebase
                            StoreEmpl emp = new StoreEmpl(strEmpId, strEmpName, strEmpDesg, strEmpMob, strEmpMail, strEmpEdu, strEmpBasicPay, strPass);
                            String key = strEmpId;
                            if (key != null) {
                                databaseReference.child(key).setValue(emp);
                                Toast.makeText(AddEmployee.this, "Employee added successfully.", Toast.LENGTH_SHORT).show();
                                finish();
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