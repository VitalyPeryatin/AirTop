package com.example.infinity.airtop.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.model.Message;

import java.util.ArrayList;

// Adapter for data management in recycler view (list of current messages)
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> currentMessages;

    
    public RecyclerViewAdapter(ArrayList<Message> currentMessages) {
        this.currentMessages = currentMessages;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_layout, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    public ArrayList<Message> getCurrentMessages() {
        return currentMessages;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.textView.setText(currentMessages.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return currentMessages.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
        }
    }
}
