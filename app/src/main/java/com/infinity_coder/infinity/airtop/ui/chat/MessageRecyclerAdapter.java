package com.infinity_coder.infinity.airtop.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> messages;
    private Context context;

    public MessageRecyclerAdapter(Context context, String uuid, ChatInteractor interactor) {
        messages = interactor.getAllMessagesByUUID(uuid);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_message, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        Message message = messages.get(position);

        setText(recyclerViewHolder.textView, message.text);
        setImage(recyclerViewHolder.imageView, message.imagePath);
        setMessageStyleByRoute(recyclerViewHolder.msgCardView, message.route);

    }

    private void setText(TextView textView, String text){
        if(text != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setText("");
            textView.setVisibility(View.GONE);
        }
    }

    private void setImage(ImageView imageView, String imagePath){
        if(imagePath != null) {
            File file = new File(imagePath);
            Picasso.get().load(file).noFade().into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
        }
    }

    private void setMessageStyleByRoute(CardView cardView, String route){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)cardView.getLayoutParams();
        int color = 0;
        switch (route){
            case Message.ROUTE_IN:
                params.gravity = Gravity.START;
                color = R.color.messageIn;
                break;
            case Message.ROUTE_OUT:
                params.gravity = Gravity.END;
                color = R.color.messageOut;
                break;
        }
        cardView.setCardBackgroundColor(context.getResources().getColor(color));
        cardView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem(Message message){
        messages.add(message);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.msgCardView)
        CardView msgCardView;
        @BindView(R.id.imageView)
        ImageView imageView;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
