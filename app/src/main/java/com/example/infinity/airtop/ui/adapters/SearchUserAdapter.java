package com.example.infinity.airtop.ui.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.chat.ChatActivity;
import com.example.infinity.airtop.ui.searchUser.SearchUserActivity;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private ArrayList<UserRequest> users = new ArrayList<>();
    private SearchUserActivity activity;

    public SearchUserAdapter(SearchUserActivity activity){
        this.activity = activity;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_search_user, viewGroup, false);
        return new SearchUserViewHolder(view);
    }

    public void updateList(ArrayList<UserRequest> list){
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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserRequest user = users.get(getAdapterPosition());
                            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
                            Addressee addressee = new Addressee(user);
                            addresseeDao.insert(addressee);
                            //App.getInstance().getListeners().getContactsAdapterListener().onUpdateList();

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ChatPresenter.getInstance().setAddresseeUser(user);
                                    Intent intent = new Intent(activity, ChatActivity.class);
                                    intent.putExtra("addresseePhone", addressee.phone);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            });

                        }
                    }).start();
                    break;
            }
        }
    }
}
