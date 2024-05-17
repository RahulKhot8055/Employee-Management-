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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.empViewHolder>{

    List<SimpleEmpData> empList;

    public ChatAdapter(List<SimpleEmpData> empList) {
        this.empList = empList;
    }

    @NonNull
    @Override
    public empViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_chat_view, parent, false);
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
                Intent intent = new Intent(view.getContext(), ChatInterface.class);
                intent.putExtra("empid",empl.getEmpId());
                intent.putExtra("empname", empl.getEmpName());
                view.getContext().startActivity(intent);
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
            empDesg = itemView.findViewById(R.id.desg);
        }
    }
}
