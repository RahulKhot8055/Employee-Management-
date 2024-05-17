package com.aditya.employeemanagement;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    List<MessageModel> msgList;
    private Context context;
    String senderId, receiverId;

    public MessageAdapter(Context context, String senderId, String receiverId){
        this.context = context;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msgList = new ArrayList<>();
    }

    public void add(MessageModel messageModel){
        msgList.add(messageModel);
        notifyDataSetChanged();
    }

    public void addAll(List<MessageModel> messages) {
        msgList.addAll(messages);
        notifyDataSetChanged();
    }

    public void clear(){
        msgList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_row, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = msgList.get(position);
        holder.msg.setText(messageModel.getMessage());

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();

        if(messageModel.getSenderId().equals(senderId)){
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_600));
            holder.msg.setTextColor(Color.BLACK);
            params.setMarginStart(80);
            params.setMarginEnd(10);
            holder.layout.setRadius(40);
            holder.layout.setLayoutParams(params);
        } else {
            holder.layout.setBackgroundColor(Color.BLACK);
            holder.msg.setTextColor(Color.WHITE);
            params.setMarginStart(10);
            params.setMarginEnd(80);
            holder.layout.setRadius(40);
            holder.layout.setLayoutParams(params);
        }
    }
    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView msg;
//        private LinearLayout main;
        private CardView layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
//            main = itemView.findViewById(R.id.msgLayout);
            layout = itemView.findViewById(R.id.cardView);
        }
    }
}
