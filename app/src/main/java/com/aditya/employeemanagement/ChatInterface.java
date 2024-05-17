package com.aditya.employeemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChatInterface extends AppCompatActivity {

    RecyclerView rcview;
    TextView msg;
    ImageButton send;
    String receiverId, senderId;

    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReceiver;
    FirebaseDatabase firebaseDatabase;

    String senderRoom, receiverRoom;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send = findViewById(R.id.sendBtn);
        rcview = findViewById(R.id.msg_recycle);
        msg = findViewById(R.id.sendmsg);

        String empName = getIntent().getStringExtra("empname");
        setTitle(empName);

        receiverId = getIntent().getStringExtra("empid");
        senderId = "manager"; // Get senderId, assuming senderId is "manager"

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;

        messageAdapter = new MessageAdapter(this, senderId, receiverId);
        rcview.setAdapter(messageAdapter);
        rcview.setLayoutManager(new LinearLayoutManager(this));

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
                    // Get current timestamp
                    long timestamp = System.currentTimeMillis();
                    sendMessage(message, timestamp);
                }
            }
        });
    }

    void sendMessage(String message, long timestamp) {
        String messageId = UUID.randomUUID().toString();
        MessageModel messageModel = new MessageModel(messageId, senderId, message,timestamp);
        messageAdapter.add(messageModel);

        // Save message to sender's and receiver's room with timestamp
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
                        Toast.makeText(ChatInterface.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        databaseReferenceReceiver.child(messageId).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message received successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to receive message
                        Toast.makeText(ChatInterface.this, "Failed to receive message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Clear the message input field after sending
        msg.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}