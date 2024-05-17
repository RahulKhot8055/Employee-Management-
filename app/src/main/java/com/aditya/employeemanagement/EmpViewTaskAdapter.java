package com.aditya.employeemanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EmpViewTaskAdapter extends RecyclerView.Adapter<EmpViewTaskAdapter.ViewHolder> {
    private List<StoreTask> taskList;

    // Constructor to initialize the adapter with data
    String empId;
    public EmpViewTaskAdapter( List<StoreTask> taskList, String empId) {
        this.taskList = taskList;
        this.empId = empId;
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskname); // Replace with your actual TextView id
            status = itemView.findViewById(R.id.status);
        }


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder instance
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_task, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to the views in each item

        holder.taskTextView.setText(taskList.get(position).getTaskName());
        holder.status.setText(taskList.get(position).getTaskStatus());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String taskId ="t"+taskList.get(position).getTaskNo();
                                // Remove item from Firebase Database dynamically using empId
                                FirebaseDatabase.getInstance().getReference()
                                        .child("EmpDetails")
                                        .child(empId) // Use dynamic empId here
                                        .child("Tasks")
                                        .child(taskId)
                                        .removeValue();
                                Toast.makeText(view.getContext(), "Deleted"+taskId+" "+empId, Toast.LENGTH_SHORT).show();
                                
                                // Remove item from RecyclerView
                                taskList.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        // Return the size of your data list
        return taskList.size();
    }
}

