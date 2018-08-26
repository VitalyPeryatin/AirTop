package com.infinity_coder.infinity.airtop.ui.searchUser;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private ArrayList<User> users = new ArrayList<>();
    private SearchUserActivity activity;
    private ChatInteractor interactor;

    SearchUserAdapter(SearchUserActivity activity){
        this.activity = activity;
        interactor = new ChatInteractor();
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
        searchUserViewHolder.imgAvatar.setImageBitmap(
                BitmapFactory.decodeResource(activity.getResources(), R.mipmap.default_avatar));
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
        @BindView(R.id.avatar_search_img)
        ImageView imgAvatar;

        SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardView)
        public void onClick(View view) {
            Contact contact = new Contact(users.get(getAdapterPosition()));
            interactor.insertAddressee(contact);
            startChatActivity(contact.uuid, contact.nickname);
        }

        // Start Chat Activity with companion by address phone
        private void startChatActivity(String addressId, String nickname){
            Intent intent = new Intent(activity, ChatActivity.class);
            String addressIdKey = activity.getString(R.string.intent_key_address_id);
            String toNicknameKey = activity.getString(R.string.nickname_key);
            intent.putExtra(addressIdKey, addressId);
            intent.putExtra(toNicknameKey, nickname);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
