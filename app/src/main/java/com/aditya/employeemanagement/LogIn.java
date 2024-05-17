package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aditya.employeemanagement.empui.EmpHome;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class LogIn extends AppCompatActivity {
    TextInputEditText username, password;
    Button logIn;
    String uname, passtxt;
    String empid, empName, empDesg, empMob, empMail, empEdu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uname = username.getText().toString().trim();
                    passtxt = password.getText().toString().trim();

                    if (uname.equals("Admin")) {
                        if (passtxt.equals("admin123")) {
                            Toast.makeText(LogIn.this, "Welcome Manager", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("empData", MODE_PRIVATE);

                            String name = "Admin";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("enname", name);
                            editor.putString("enpass", passtxt);
                            editor.commit();

                            Intent intent = new Intent(LogIn.this, Home.class);
                            startActivity(intent);
                            username.setText("");
                            password.setText("");
                            finish();
                        } else {
                            Toast.makeText(LogIn.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                            password.requestFocus();
                            return;
                        }
                    } else {
                        loginEmp(uname, passtxt);
                    }
                }
            });
    }

    public void loginEmp(String uname, String passw){
        DatabaseReference empRef = FirebaseDatabase.getInstance().getReference().child("EmpDetails").child(uname);

        empRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Employee exists
                    String storedPass = snapshot.child("strEmpPass").getValue(String.class);
                    empName = snapshot.child("strEmpName").getValue(String.class);
                    empDesg = snapshot.child("strEmpDesg").getValue(String.class);
                    empMail = snapshot.child("strEmpMail").getValue(String.class);
                    empMob = snapshot.child("strEmpMob").getValue(String.class);
                    empEdu = snapshot.child("strEmpEdu").getValue(String.class);

                    if (storedPass.equals(passw)) {
                        // Password matches
                        Log.d("EmployeeLogin", "Employee with empId " + uname + " exists and password matches.");
                        Toast.makeText(LogIn.this, "Log in successful", Toast.LENGTH_SHORT).show();

                        //storing into shared preferences.
                        SharedPreferences sharedPreferences = getSharedPreferences("empData", MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("enname", uname);
                        editor.putString("enpass", passw);
                        editor.putString("empname", empName);
                        editor.putString("empDesg", empDesg);
                        editor.putString("empMob", empMob);
                        editor.putString("empMail", empMail);
                        editor.putString("empEdu", empEdu);
                        Toast.makeText(LogIn.this, "Added "+empName+empMob+empEdu, Toast.LENGTH_SHORT).show();

                        editor.commit();

                        Intent intent = new Intent(LogIn.this, EmpHome.class);
                        intent.putExtra("emid", uname);
                        Toast.makeText(LogIn.this, "id "+uname+" name"+empName+" desg"+empDesg+" mob"+empMob, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } else {
                        // Password does not match
                        Log.d("EmployeeLogin", "Employee with empId " + uname + " exists but password does not match.");
                        Toast.makeText(LogIn.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Employee does not exist
                    Log.d("EmployeeLogin", "Employee with empId " + uname + " does not exist.");
                    Toast.makeText(LogIn.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

}