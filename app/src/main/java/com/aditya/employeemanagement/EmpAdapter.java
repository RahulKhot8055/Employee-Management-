package com.aditya.employeemanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.empViewHolder>{

    List<SimpleEmpData> empList;

    public EmpAdapter(List<SimpleEmpData> empList) {
        this.empList = empList;
    }

    @NonNull
    @Override
    public empViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_design, parent, false);
        return new empViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull empViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.empName.setText(empList.get(position).getEmpName());
        holder.empDesg.setText(empList.get(position).getEmpDesg());
        SimpleEmpData empl =empList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Profile.class);
                intent.putExtra("empname",empl.getEmpName());
                intent.putExtra("empDesg",empl.getEmpDesg());
                intent.putExtra("empId", empl.getEmpId());
                intent.putExtra("empMob",empl.getEmpMob());
                intent.putExtra("empMail", empl.getEmpMail());
                intent.putExtra("empEdu",empl.getEmpEdu());


                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Employee");
                builder.setMessage("Want to delete-"+empList.get(position).getEmpName()+" \n\nAre you sure...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String empId = empList.get(position).getEmpId();
                        empList.remove(position);
                        FirebaseDatabase.getInstance().getReference().child("EmpDetails")
                                .child(empId).removeValue();
                        Toast.makeText(view.getContext(), "Employee " + empId + " deleted successfully. ", Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(empId+"manager").removeValue();

                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child("manager"+empId).removeValue();

                        // Notify adapter about the item removal
                        notifyDataSetChanged();
                    }
                });

builder.setNegativeButton("No",(dialogInterface, i) -> {});
            builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public static class empViewHolder extends  RecyclerView.ViewHolder{
        TextView empName, empDesg;
        public empViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name);
            empDesg = itemView.findViewById(R.id.textView4);
        }
    }
}