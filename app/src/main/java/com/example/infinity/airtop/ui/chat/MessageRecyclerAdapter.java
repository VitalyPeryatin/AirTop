package com.example.infinity.airtop.ui.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> currentMessageRequests;

    public MessageRecyclerAdapter(String uuid, ChatInteractor interactor) {
        currentMessageRequests = interactor.getAllMessagesByUUID(uuid);
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
        //Bitmap image;

        //ImageView imageView = recyclerViewHolder.imageView;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                recyclerViewHolder.msgCardView.getLayoutParams();

        // Set text relatively received messages
        if((text = currentMessageRequests.get(position).text) != null) {
            recyclerViewHolder.textView.setText(text);
            recyclerViewHolder.textView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewHolder.textView.setText("");
            recyclerViewHolder.textView.setVisibility(View.GONE);
        }

        // Set display type by route relatively received messages
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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.msgCardView)
        CardView msgCardView;
        /*@BindView(R.id.imageView)
        ImageView imageView;*/

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
