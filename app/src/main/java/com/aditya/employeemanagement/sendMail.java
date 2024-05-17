package com.aditya.employeemanagement;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import papaya.in.sendmail.SendMail;

public class sendMail extends Fragment {


    TextInputEditText selctmails, mSubject, mMessage, fileAtach, fromMail;
    Button chEmpBtn, sendAnc;

    private static final int REQUEST_CODE = 100;
    private static final int EMAIL_SEND_REQUEST_CODE = 123; // Define a request code

    Uri uri=null;

    List<Uri> selectedUris = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_mail, container, false);

        selctmails = view.findViewById(R.id.chparticipant);
        mSubject = view.findViewById(R.id.mailsub);
        mMessage = view.findViewById(R.id.mailmsg);
        fileAtach = view.findViewById(R.id.fileatch);
        sendAnc = view.findViewById(R.id.sendAnc);
        fromMail = view.findViewById(R.id.mailFrom);
        fromMail.setEnabled(false);

        View alertCustomDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_emp,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setView(alertCustomDialog);
//        Button btn = alertCustomDialog.findViewById(R.id.sendsms);
        final AlertDialog dialog = alertDialog.create();

        RecyclerView rcView = alertCustomDialog.findViewById(R.id.selectEmpRcview);
        CheckBox selectAll;
        selectAll = alertCustomDialog.findViewById(R.id.selectAll);
        chEmpBtn = alertCustomDialog.findViewById(R.id.sendsms);
        final SendSmsEmpAdapter[] empAdapter = new SendSmsEmpAdapter[1];

        List<SimpleEmpData> list;
        list = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("EmpDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String empName = dataSnapshot.child("strEmpName").getValue(String.class);
                            String desg = dataSnapshot.child("strEmpDesg").getValue(String.class);
                            String empId = dataSnapshot.child("strEmpId").getValue(String.class);
                            String mob = dataSnapshot.child("strEmpMob").getValue(String.class);
                            String mail = dataSnapshot.child("strEmpMail").getValue(String.class);
                            String edu = dataSnapshot.child("strEmpEdu").getValue(String.class);
                            String basePay = dataSnapshot.child("strEmpBasicPay").getValue(String.class);
//                                Toast.makeText(ManageEmployee.this, "Name: " + empName + " desg: " + desg, Toast.LENGTH_SHORT).show();
                            list.add(new SimpleEmpData(empName,desg,empId,mob,mail, edu, basePay));
                        }

                        // Set RecyclerView adapter here after adding all data
                        rcView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
                        empAdapter[0] = new SendSmsEmpAdapter(list);
                        rcView.setAdapter(empAdapter[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    empAdapter[0].checkAll();
                }else{
                    empAdapter[0].unCheckAll();
                }
            }
        });
        chEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] checkedItems = empAdapter[0].getCheckedItems();
                List<SimpleEmpData> selectedItems = new ArrayList<>();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedItems.add(empAdapter[0].empList.get(i));
                    }
                }

                StringBuilder namesBuilder = new StringBuilder();
                for (SimpleEmpData item : selectedItems) {
                    namesBuilder.append(item.getEmpMail()).append(", ");
                }
                selctmails.setText(namesBuilder.toString());
                dialog.dismiss();
            }
        });

        selctmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chEmpBtn.setText("Choose Participant");
                dialog.show();
            }
        });



        fileAtach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        if (fileAtach != null) {
            // Set a click listener for the remove button
            fileAtach.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP &&
                            event.getRawX() >= (fileAtach.getRight() - fileAtach.getCompoundDrawables()[2].getBounds().width())) {
                        // Clear the list of selected files and update the UI
                        selectedUris.clear();
                        fileAtach.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }

        sendAnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailWithAttachment(selectedUris);
            }
        });

        return  view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String fileName="";

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            selectedUris.clear();
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        selectedUris.add(uri);
                    }
                } else if (data.getData() != null) {
                    // Single file selected
                    Uri uri = data.getData();
                    selectedUris.add(uri);
                }

                // Handle selected files
                handleSelectedFiles(selectedUris);
            }
        }
        if (requestCode == EMAIL_SEND_REQUEST_CODE) {
            // Check if the result is OK
            if (resultCode == Activity.RESULT_OK) {
                // Clear the text of the subject and message text boxes
                mSubject.setText("");
                mMessage.setText("");
                selctmails.setText("");
            }
        }
    }

    private void handleSelectedFiles(List<Uri> selectedUris) {
        String selectedFilePath="";
        // Process each selected file URI
        for (Uri uri : selectedUris) {
            // Here, you can use 'uri' to access the selected file
            selectedFilePath += getFileNameFromUri(uri)+" | ";
        }
        fileAtach.setText(selectedFilePath);
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }

    private void sendEmailWithAttachment(List<Uri> fileUris) {

        String strSubj = mSubject.getText().toString().trim();
        String strMsg = mMessage.getText().toString().trim();

        if(selctmails.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please choose whom to send mail", Toast.LENGTH_SHORT).show();
            selctmails.requestFocus();
            return;
        }

        if(strSubj.isEmpty()){
            Toast.makeText(getContext(), "Enter subject of your mail", Toast.LENGTH_SHORT).show();
            mSubject.requestFocus();
            return;
        }

        if(strMsg.isEmpty()){
            Toast.makeText(getContext(), "Enter body of your mail", Toast.LENGTH_SHORT).show();
            mMessage.requestFocus();
            return;
        }

//        Toast.makeText(getContext(), ""+selctmails.getText(), Toast.LENGTH_SHORT).show();

        String[] recepants = selctmails.getText().toString().split(",");

        if (fileUris != null && !fileUris.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recepants);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, strSubj);
            emailIntent.putExtra(Intent.EXTRA_TEXT, strMsg);

            // Handle the case where the list of file URIs is not null and not empty
            Toast.makeText(getContext(), "Files attached.", Toast.LENGTH_SHORT).show();
            ArrayList<Uri> uris = new ArrayList<>(fileUris);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            Intent chooserIntent = Intent.createChooser(emailIntent, "Send email...");
            chooserIntent.putExtra(Intent.EXTRA_EMAIL, emailIntent); // Ensure emailIntent is included in the chooser

            try {
                startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }else{
            for(String rec: recepants){
                SendMail mail = new SendMail(fromMail.getText().toString(), "bzzd lxic cwig frhk",
                        rec,
                        strSubj,
                        strMsg);
                mail.execute();
            }
        }

    }
}