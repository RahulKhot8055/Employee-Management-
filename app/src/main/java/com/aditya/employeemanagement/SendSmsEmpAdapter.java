package com.aditya.employeemanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SendSmsEmpAdapter extends RecyclerView.Adapter<SendSmsEmpAdapter.empViewHolder>{

    List<SimpleEmpData> empList;
    private boolean[] checkedItems;
    CheckBox selectAll;
    public SendSmsEmpAdapter(List<SimpleEmpData> empList) {
        this.empList = empList;
        checkedItems = new boolean[empList.size()];
        Arrays.fill(checkedItems,false);
    }
    public void unCheckAll() {
        Arrays.fill(checkedItems, false);
        notifyDataSetChanged(); // Notify RecyclerView to update views
    }
    public void checkAll(){
        Arrays.fill(checkedItems, true);
        notifyDataSetChanged();
    }
    public boolean[] getCheckedItems() {
        return checkedItems;
    }
    @NonNull
    @Override
    public empViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_emp_design, parent, false);
        return new empViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull empViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.empName.setText(empList.get(position).getEmpName());
        holder.empDesg.setText(empList.get(position).getEmpDesg());

        holder.checkBox.setOnCheckedChangeListener(null); // To prevent triggering listener when recycled view is reused
        holder.checkBox.setChecked(checkedItems[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                }else{
                    holder.checkBox.setChecked(true);
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checkedItems[position] = b;
                else
                    unCheckAll();
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public static class empViewHolder extends  RecyclerView.ViewHolder{
        TextView empName, empDesg;
        CheckBox checkBox, selectAll;
        public empViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name2);
            empDesg = itemView.findViewById(R.id.empDesg2);
            checkBox = itemView.findViewById(R.id.selectEmpChkBox);
        }
    }
}