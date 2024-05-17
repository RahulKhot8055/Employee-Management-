package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageEmployee extends AppCompatActivity {

    FloatingActionButton addEmp;
    RecyclerView rcView;
    EmpAdapter empAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addEmp = findViewById(R.id.fab);
        rcView = findViewById(R.id.employees);
        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageEmployee.this, AddEmployee.class);
                startActivity(intent);
            }
        });

        displayEmps();
    }

    void displayEmps(){

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
                            String mob = dataSnapshot.child("strEmpMob").getValue(String.class);
                            String mail = dataSnapshot.child("strEmpMail").getValue(String.class);
                            String edu = dataSnapshot.child("strEmpEdu").getValue(String.class);
                            String basePay = dataSnapshot.child("strEmpBasicPay").getValue(String.class);
//                                Toast.makeText(ManageEmployee.this, "Name: " + empName + " desg: " + desg, Toast.LENGTH_SHORT).show();
                            list.add(new SimpleEmpData(empName,desg,empId,mob,mail, edu, basePay));
                        }

                        // Set RecyclerView adapter here after adding all data
                        rcView.setLayoutManager(new LinearLayoutManager(ManageEmployee.this, LinearLayoutManager.VERTICAL,false));
                        empAdapter= new EmpAdapter(list);
                        rcView.setAdapter(empAdapter);
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