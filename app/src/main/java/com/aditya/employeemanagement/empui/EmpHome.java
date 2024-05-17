package com.aditya.employeemanagement.empui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aditya.employeemanagement.Attendance;
import com.aditya.employeemanagement.Home;
import com.aditya.employeemanagement.MyViewPageAdapter;
import com.aditya.employeemanagement.Profile;
import com.aditya.employeemanagement.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpHome extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    empHomeViewPageAdapter myViewPageAdapter;
    String empid, empName, empDesg, empMob, empMail, empEdu;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);

        tabLayout = findViewById(R.id.optionsemp);
        viewPager2 = findViewById(R.id.viewPagerEmp);
        myViewPageAdapter = new empHomeViewPageAdapter(this);
        viewPager2.setAdapter(myViewPageAdapter);

        imageView = findViewById(R.id.imageView4);

        Bundle extras = getIntent().getExtras();

        SharedPreferences sh = getSharedPreferences("empData", Context.MODE_PRIVATE);
        empid = sh.getString("enname", "");
        empName = sh.getString("empname","");
        empDesg = sh.getString("empDesg","");
        empMob = sh.getString("empMob","");
        empMail = sh.getString("empMail","");
        empEdu = sh.getString("empEdu","");

        Toast.makeText(this, "name: "+empName+" \ndesg"+empDesg, Toast.LENGTH_SHORT).show();

        if(empid!=null){
            Toast.makeText(this, "Empid: "+empid, Toast.LENGTH_SHORT).show();
            setTitle(empid);
        }

//        getEmpInfo();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
                if(position==0){
                    imageView.setVisibility(View.GONE);
                }else{
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                final SharedPreferences sharedPreferences = getSharedPreferences("empData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(EmpHome.this, "Logged out", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.prof:
                Toast.makeText(this, "Clicked profile.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EmpHome.this, Profile.class);
                intent.putExtra("empname", empName);
                intent.putExtra("empId",empid);
                intent.putExtra("empDesg",empDesg);
                intent.putExtra("empId", empid);
                intent.putExtra("empMob",empMob);
                intent.putExtra("empMail", empMail);
                intent.putExtra("empEdu",empEdu);
                startActivity(intent);
                return true;

            case R.id.attendance:
                Toast.makeText(this, "Attendance", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(EmpHome.this, EmpAttendance.class);
                intent1.putExtra("empId", empid);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void getData(){
        empName = getIntent().getStringExtra("empname");

    }

    public void onBackPressed() {
        if(true){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EmpHome.this);

            // Set the message show for the Alert time
            builder.setMessage("Do you want to exit ?");

            // Set Alert Title
            builder.setTitle("Alert !");

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                // When the user click yes button then app will close
                finish();
            });

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }else
            super.onBackPressed();
    }
}