package com.example.infinity.airtop.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.data.network.MessageRequest;

import java.util.ArrayList;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.0
 */
public class MessageListViewAdapter extends RecyclerView.Adapter<MessageListViewAdapter.RecyclerViewHolder> {
    
    private ArrayList<MessageRequest> currentMessageRequests = new ArrayList<>();
    
    public MessageListViewAdapter(String phone) {
        new Thread(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Message> messages = (ArrayList<Message>) messageDao.getBySenderPhone(phone);
            Log.d("phone", phone);
            for (Message message : messages) {
                currentMessageRequests.add(new MessageRequest(message));
            }
        }).start();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_message, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        String text;
        Bitmap image;
        TextView textView = recyclerViewHolder.textView;
        ImageView imageView = recyclerViewHolder.imageView;

        if((text = currentMessageRequests.get(position).getText()) != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewHolder.textView.setText("");
            textView.setVisibility(View.GONE);
        }

        if((image = currentMessageRequests.get(position).getImage()) != null) {
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
        return currentMessageRequests.size();
    }

    public void addItem(MessageRequest messageRequest){
        currentMessageRequests.add(messageRequest);

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
