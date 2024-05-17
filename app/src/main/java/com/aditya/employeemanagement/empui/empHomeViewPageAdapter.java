package com.aditya.employeemanagement.empui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aditya.employeemanagement.sendMail;
import com.aditya.employeemanagement.sendSMS;

public class empHomeViewPageAdapter extends FragmentStateAdapter {

    public empHomeViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new EmpChat();
           case 1:
               return new EmpTasks();
       }
       return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
