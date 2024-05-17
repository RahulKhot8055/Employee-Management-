package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewTasks extends AppCompatActivity {

    TextInputEditText taskText;
    TextView empText;
    Button add;
    String empId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView rcView;
    EmpViewTaskAdapter empAdapter;
    ImageView task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        Bundle extras = getIntent().getExtras();
        String name="loading...";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskText = findViewById(R.id.tasktext);
        add = findViewById(R.id.addTask);
        empText = findViewById(R.id.textView6);
        rcView = findViewById(R.id.tasksView);
        task = findViewById(R.id.imageView6);

            name = extras.getString("ename");
            empId = extras.getString("empid");

        name = name.toUpperCase();
        empText.setText(name);

        viewTasksList();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
                taskText.setText("");
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

    public void viewTasksList(){

        List<StoreTask> list;
        list = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("EmpDetails").child(empId).child("Tasks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String taskName = dataSnapshot.child("taskName").getValue(String.class);
                            String status = dataSnapshot.child("taskStatus").getValue(String.class);
                            String taskNo = dataSnapshot.child("taskNo").getValue(String.class);

//                                Toast.makeText(ManageEmployee.this, "Name: " + empName + " desg: " + desg, Toast.LENGTH_SHORT).show();
                            list.add(new StoreTask(taskName,taskNo, status));
                        }

                        if (list.isEmpty()) {
                            // Show a toast message indicating no tasks assigned
                            Toast.makeText(ViewTasks.this, "No tasks assigned.", Toast.LENGTH_SHORT).show();
                            rcView.setVisibility(View.GONE);
                            task.setVisibility(View.VISIBLE);
                        } else {
                            // Set RecyclerView adapter here after adding all data

                            rcView.setLayoutManager(new LinearLayoutManager(ViewTasks.this, LinearLayoutManager.VERTICAL, false));
                            empAdapter = new EmpViewTaskAdapter(list,empId);
                            rcView.setAdapter(empAdapter);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
    }


    public void addTask(){
        String taskStr = taskText.getText().toString();
        if(taskStr.isEmpty()){
            Toast.makeText(this, "Task should not be empty", Toast.LENGTH_SHORT).show();
            taskText.requestFocus();
            return;
        }

        // Get a reference to the 'Tasks' node for the specific employee
        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference()
                .child("EmpDetails")
                .child(empId)
                .child("Tasks");

        // Fetch the current task count just once using addListenerForSingleValueEvent()
        tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long taskCount = snapshot.getChildrenCount();
                String nextTask = "t" + (taskCount + 1);
                String taskNo = String.valueOf(taskCount+1);

                // Create the StoreTask object
                StoreTask task = new StoreTask(taskStr, taskNo, "Assigned");

                // Add the new task to the database
                tasksRef.child(nextTask).setValue(task);

                // Show a toast message indicating successful task addition
                Toast.makeText(ViewTasks.this, "Task added successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

}