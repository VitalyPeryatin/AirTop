package com.infinity_coder.infinity.airtop.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.network.Image;
import com.infinity_coder.infinity.airtop.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.infinity_coder.infinity.airtop.ui.chat.MessageRecyclerAdapter.RecyclerViewHolder.MAX_HEIGHT;
import static com.infinity_coder.infinity.airtop.ui.chat.MessageRecyclerAdapter.RecyclerViewHolder.MAX_WIDTH;
import static com.infinity_coder.infinity.airtop.ui.chat.MessageRecyclerAdapter.RecyclerViewHolder.RATIO;

/**
 *  Adapter for data management in recycler view (list of current messages)
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.RecyclerViewHolder> {
    
    private ArrayList<Message> messages;
    private Context context;

    MessageRecyclerAdapter(Context context, String uuid, ChatInteractor interactor) {
        this.messages = interactor.getAllMessagesByUUID(uuid);
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

        int radius = (int) recyclerViewHolder.msgCardView.getRadius();
        setText(recyclerViewHolder.textView, message.text);
        setImage(recyclerViewHolder.imageView, message.getImage(), radius);
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

    private void setImage(ImageView imageView, Image image, int radius){
        if(image.getName() != null) {
            setImageViewParams(imageView, image);
            File file = new File(App.imagePath + image.getName());
            Picasso.get()
                    .load(file)
                    .transform(new RoundedTransformation(radius))
                    .noFade()
                    .fit()
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
        }
    }

    private void setImageViewParams(ImageView imageView, Image image){
        float height = image.getHeight();
        float width = image.getWidth();
        int maxHeightPx = convertDpToPx(MAX_HEIGHT);
        int maxWidthPx = convertDpToPx(MAX_WIDTH);
        if(height / width >= RATIO){
            imageView.getLayoutParams().height = maxHeightPx;
            imageView.getLayoutParams().width = (int) (maxWidthPx * (height / maxHeightPx));
        }
        else{
            imageView.getLayoutParams().width = maxWidthPx;
            imageView.getLayoutParams().height = (int) (maxHeightPx * (maxWidthPx / width));
        }
        imageView.requestLayout();
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
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.msgCardView)
        CardView msgCardView;
        @BindView(R.id.imageViewMsg)
        ImageView imageView;

        static final int MAX_HEIGHT = 336;
        static final int MAX_WIDTH = 296;
        static final int RATIO = MAX_HEIGHT / MAX_WIDTH;


        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imageView.setMaxHeight(convertDpToPx(MAX_HEIGHT));
            imageView.setMaxWidth(convertDpToPx((MAX_WIDTH)));
        }
    }

    private int convertDpToPx(int dp){
        DisplayMetrics metrics = App.getInstance().getBaseContext().getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
