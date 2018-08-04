package com.example.infinity.airtop.ui.chat;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;

import java.util.ArrayList;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.0
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> currentMessageRequests = new ArrayList<>();
    
    public MessageRecyclerAdapter(String phone) {
        new Thread(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            currentMessageRequests = (ArrayList<Message>) messageDao.getByAddresseePhone(phone);
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
        String route = currentMessageRequests.get(position).route;
        Bitmap image;

        ImageView imageView = recyclerViewHolder.imageView;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                recyclerViewHolder.msgCardView.getLayoutParams();

        if((text = currentMessageRequests.get(position).text) != null) {
            recyclerViewHolder.textView.setText(text);
            recyclerViewHolder.textView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewHolder.textView.setText("");
            recyclerViewHolder.textView.setVisibility(View.GONE);
        }

        if(route.equals(Message.ROUTE_IN))
            params.gravity = Gravity.END;
        else if(route.equals(Message.ROUTE_OUT))
            params.gravity = Gravity.START;
        recyclerViewHolder.msgCardView.setLayoutParams(params);

        /*if((image = currentMessageRequests.get(position).getImage()) != null) {
            imageView.setImageBitmap(image);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return currentMessageRequests.size();
    }

    public void addItem(Message message){
        currentMessageRequests.add(message);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        CardView msgCardView;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            msgCardView = itemView.findViewById(R.id.msgCardView);
        }
    }
}
