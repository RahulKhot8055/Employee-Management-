package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rcView;
    ChatAdapter empAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rcView = findViewById(R.id.showempchats);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                        rcView.setLayoutManager(new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL,false));
                        empAdapter= new ChatAdapter(list);
                        rcView.setAdapter(empAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;

            case R.id.search:
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}