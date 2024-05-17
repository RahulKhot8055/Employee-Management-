package com.aditya.employeemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// this class is created for showing the attendance on dialog box when long press on name is done by employee and attendance will be displayed.

public class AttEmpAdapter extends RecyclerView.Adapter<AttEmpAdapter.attEMpViewHolder> {

    List<SimpleEmpData> empList;

    public AttEmpAdapter(List<SimpleEmpData> empList){this.empList = empList;}

    @NonNull
    @Override
    public attEMpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_att_emp_view, parent,false);
        return new attEMpViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull attEMpViewHolder holder, int position) {
        holder.empName.setText(empList.get(position).getEmpName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                showCustomDialog(empList.get(position), view);
                return true;
            }
        });
    }

    private void showCustomDialog(SimpleEmpData empData, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_att, null);

        RecyclerView rcView = dialogView.findViewById(R.id.rcViewAtt);
        List<AttData> list;
        list = new ArrayList<>();
        final AttAdapter[] attAdapter = new AttAdapter[1];

        // Retrieve attendance data from Firebase for the specific employee (empId)
        String empId = empData.getEmpId();
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
                rcView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
                attAdapter[0] = new AttAdapter(list);
                rcView.setAdapter(attAdapter[0]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event if needed
            }
        });

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform any action needed when OK is clicked
                // For example, you can dismiss the dialog or perform further operations
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public int getItemCount() {
        return empList.size();
    }
    public static class attEMpViewHolder extends RecyclerView.ViewHolder{
        TextView empName;
        public attEMpViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name_att);

        }
    }
}
