package com.example.infinity.airtop.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.model.Message;

import java.util.ArrayList;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.0
 */
public class MessageListViewAdapter extends RecyclerView.Adapter<MessageListViewAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> currentMessages;

    
    MessageListViewAdapter(ArrayList<Message> currentMessages) {
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
        String text;
        Bitmap image;
        TextView textView = recyclerViewHolder.textView;
        ImageView imageView = recyclerViewHolder.imageView;

        if((text = currentMessages.get(position).getText()) != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewHolder.textView.setText("");
            textView.setVisibility(View.GONE);
        }

        if((image = currentMessages.get(position).getImage()) != null) {
            imageView.setImageBitmap(image);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return currentMessages.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
