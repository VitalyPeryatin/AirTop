package com.example.infinity.airtop.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.ChatPresenter;
import com.example.infinity.airtop.presenters.Presenter;
import com.example.infinity.airtop.views.ChatActivity;
import com.example.infinity.airtop.views.SearchUserActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private ArrayList<User> users = new ArrayList<>();
    private SearchUserActivity activity;

    public SearchUserAdapter(SearchUserActivity activity){
        this.activity = activity;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_search_user, viewGroup, false);
        return new SearchUserViewHolder(view);
    }

    public void updateList(ArrayList<User> list){
        users.clear();
        users.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        users.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder searchUserViewHolder, int i) {
        searchUserViewHolder.tvUsername.setText(users.get(i).username);
        searchUserViewHolder.tvPhone.setText(users.get(i).phone);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SearchUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvUsername, tvPhone;
        CardView cardView;

        SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cardView:
                    User user = users.get(getAdapterPosition());
                    ChatPresenter.getInstance().setAddresseeUser(user);
                    Intent intent = new Intent(activity, ChatActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    break;
            }
        }
    }
}
