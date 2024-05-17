package com.aditya.employeemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttAdapter extends  RecyclerView.Adapter<AttAdapter.attViewHolder> {
    List<AttData> empList;

    public AttAdapter(List<AttData> empList){this.empList = empList;}

    @NonNull
    @Override
    public AttAdapter.attViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_att, parent,false);
        return  new AttAdapter.attViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull AttAdapter.attViewHolder holder, int position) {
        holder.date.setText(empList.get(position).getDate());
        holder.status.setText(empList.get(position).getStatus().toUpperCase());
        if(empList.get(position).getStatus().equalsIgnoreCase("PRESENT")){
            holder.status.setTextColor(Color.rgb(65,140,70));
        }else{
            holder.status.setTextColor(Color.RED);
        }
    }
    @Override
    public int getItemCount() {
        return empList.size();
    }
    public static class attViewHolder extends RecyclerView.ViewHolder{
        TextView date, status;
        public attViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_att);
            status = itemView.findViewById(R.id.status_att);
        }
    }
}