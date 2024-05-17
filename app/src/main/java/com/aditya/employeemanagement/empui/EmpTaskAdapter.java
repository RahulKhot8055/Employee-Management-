package com.aditya.employeemanagement.empui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.employeemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class EmpTaskAdapter extends RecyclerView.Adapter<EmpTaskAdapter.ViewHolder> {
    private List<StoreEmpTask> taskList;
    private String[] spinnerOptions = {"In progress", "Done"};
    public EmpTaskAdapter(List<StoreEmpTask> taskLi){
        this.taskList = taskLi;
    }

    @NonNull
    @Override
    public EmpTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_emp_task, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull EmpTaskAdapter.ViewHolder holder, int position) {

        holder.taskText.setText(taskList.get(position).getTaskName());
        holder.taskNo.setText(taskList.get(position).getTaskNo());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(holder.itemView.getContext(),R.layout.spinnner_tem_layout, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerAdapter);

//        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Context context = view.getContext();
//                String selectedItem = (String) holder.spinner.getSelectedItem();
//
//                SharedPreferences sharedPreferences = context.getSharedPreferences("empData", Context.MODE_PRIVATE);
//                String name = sharedPreferences.getString("enname", "");
//
//                // Get the current StoreEmpTask object
//                StoreEmpTask currentTask = taskList.get(holder.getAdapterPosition());
//
//                DatabaseReference taskStatusRef = FirebaseDatabase.getInstance().getReference()
//                        .child("EmpDetails").child(name).child("Tasks").child("t" + currentTask.getTaskNo()).child("taskStatus");
//
//
//                // Update the taskStatus value based on the selected item
//                taskStatusRef.setValue(selectedItem)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // TaskStatus updated successfully
//                                Toast.makeText(context, "TaskStatus updated successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Failed to update taskStatus
//                                Toast.makeText(context, "Failed to update TaskStatus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                // Show a toast message with the selected item
//                Toast.makeText(view.getContext(), "Selected item: " + selectedItem + " Emp id: " + name + " Task no: " + "t" + currentTask.getTaskNo(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                // Handle the case where nothing is selected
//            }
//        });

    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView taskText, taskNo;
        Spinner spinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskText = itemView.findViewById(R.id.taskname);
            taskNo = itemView.findViewById(R.id.taskNo);
            spinner = itemView.findViewById(R.id.spinner);
        }
    }
}
