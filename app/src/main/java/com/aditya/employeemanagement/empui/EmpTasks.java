package com.aditya.employeemanagement.empui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.aditya.employeemanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpTasks extends Fragment {

    RecyclerView rcView;
    String currEmpId;
    EmpTaskAdapter empTaskAdapter;
    ImageView noTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emp_tasks, container, false);
        rcView = view.findViewById(R.id.rcViewEmp);
        noTask = view.findViewById(R.id.imageView6);

        currEmpId = getActivity().getTitle().toString();
        Toast.makeText(getContext(), " "+currEmpId, Toast.LENGTH_SHORT).show();

        showTaskList();


        return view;
    }

    public void showTaskList(){
        List<StoreEmpTask> list;
        list = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("EmpDetails").child(currEmpId).child("Tasks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot datasn: snapshot.getChildren()){
                            String taskName = datasn.child("taskName").getValue(String.class);
                            String taskNo = datasn.child("taskNo").getValue(String.class);
                            list.add(new StoreEmpTask(taskName,taskNo));
                        }
                        if(list.isEmpty()){
                            Toast.makeText(getContext(), "No tasks assigned.", Toast.LENGTH_SHORT).show();
                            rcView.setVisibility(View.GONE);
                            noTask.setVisibility(View.VISIBLE);

                        }else{
                            rcView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            empTaskAdapter = new EmpTaskAdapter(list);
                            rcView.setAdapter(empTaskAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}