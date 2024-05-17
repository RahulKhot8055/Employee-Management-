package com.aditya.employeemanagement.empui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aditya.employeemanagement.MessageAdapter;
import com.aditya.employeemanagement.MessageModel;
import com.aditya.employeemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class EmpChat extends Fragment {
    RecyclerView rcview;
    TextView msg;
    ImageButton send;
    String receiverId, senderId; // Add senderId
    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReceiver;
    FirebaseDatabase firebaseDatabase;

    String senderRoom, receiverRoom;
    MessageAdapter messageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emp_chat, container, false);

        send = view.findViewById(R.id.sendBtn);
        rcview = view.findViewById(R.id.msg_recycle);
        msg = view.findViewById(R.id.sendmsg);

        receiverId = "manager"; // Get senderId, assuming senderId is "manager"
        senderId = getActivity().getTitle().toString(); // Get senderId from activity title

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;

        messageAdapter = new MessageAdapter(view.getContext(), senderId, receiverId);
        rcview.setAdapter(messageAdapter);
        rcview.setLayoutManager(new LinearLayoutManager(view.getContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceSender = firebaseDatabase.getReference("chats").child(senderRoom);
        databaseReferenceReceiver = firebaseDatabase.getReference("chats").child(receiverRoom);
        databaseReferenceSender.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MessageModel messageModel = data.getValue(MessageModel.class);
                    messages.add(messageModel);
                }
                // Sort messages based on timestamp
                Collections.sort(messages, new Comparator<MessageModel>() {
                    @Override
                    public int compare(MessageModel o1, MessageModel o2) {
                        return Long.compare(o1.getTimestamp(), o2.getTimestamp());
                    }
                });
                // Add all messages to adapter
                messageAdapter.addAll(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = msg.getText().toString().trim();
                if (!message.isEmpty()) {
                    long timestamp = System.currentTimeMillis(); // Get current timestamp
                    sendMessage(message,timestamp);
                }
            }
        });

        return view;
    }
    void sendMessage(String message, long timeStamp) {
        String messageId = UUID.randomUUID().toString();

        MessageModel messageModel = new MessageModel(messageId, senderId, message,timeStamp);

        messageAdapter.add(messageModel);

        databaseReferenceSender.child(messageId).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message sent successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to send message
                    }
                });

        databaseReferenceReceiver.child(messageId).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message rece ived successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to receive message
                    }
                });

        // Clear the message input field after sending
        msg.setText("");
    }
}