package com.example.infinity.airtop.ui.searchUser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.SearchUserInteractor;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private ArrayList<User> users = new ArrayList<>();
    private SearchUserActivity activity;
    private SearchUserInteractor interactor;

    SearchUserAdapter(SearchUserActivity activity){
        this.activity = activity;
        interactor = new SearchUserInteractor();
    }

    @NonNull
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
        searchUserViewHolder.tvNickname.setText(users.get(i).nickname);
        searchUserViewHolder.tvUsername.setText(users.get(i).username);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SearchUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvNickname)
        TextView tvNickname;
        @BindView(R.id.tvUsername)
        TextView tvUsername;

        SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        @OnClick(R.id.cardView)
        public void onClick(View view) {
            Addressee addressee = new Addressee(users.get(getAdapterPosition()));
            interactor.insertAddressee(addressee);
            startChatActivity(addressee.uuid);
        }

        // Start Chat Activity with companion by address phone
        private void startChatActivity(String addressId){
            Intent intent = new Intent(activity, ChatActivity.class);
            String addressIdKey = activity.getResources().getString(R.string.intent_key_address_id);
            intent.putExtra(addressIdKey, addressId);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
