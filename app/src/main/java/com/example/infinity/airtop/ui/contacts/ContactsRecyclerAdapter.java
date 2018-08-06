package com.example.infinity.airtop.ui.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ContactsInteractor;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactsViewHolder> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private Fragment fragment;
    private Context context;
    private ContactsInteractor interactor = new ContactsInteractor();

    public ContactsRecyclerAdapter(Fragment fragment){
        this.fragment = fragment;
        context = App.getInstance().getBaseContext();
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_contact, viewGroup, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i) {
        contactsViewHolder.tvAddressTitle.setText(contacts.get(i).getAddressee().username);
        contactsViewHolder.tvLastMessage.setText(contacts.get(i).getLastMessage());
    }

    public void onUpdateList(){ // TODO заменить полное изменени списка на локальные изменения
        contacts = interactor.getContacts();
        notifyDataSetChanged();
    }

    public void addContactByAddress(Addressee addressee){

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvAddressTitle, tvLastMessage;
        LinearLayout container;
        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddressTitle = itemView.findViewById(R.id.addressTitle);
            tvLastMessage = itemView.findViewById(R.id.lastMsgText);
            container = itemView.findViewById(R.id.container);

            container.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.container:
                    Addressee addressee = contacts.get(getAdapterPosition()).getAddressee();
                    Intent intent = new Intent(context, ChatActivity.class);
                    String addressPhone = context.getResources().getString(R.string.intent_key_address_phone);
                    intent.putExtra(addressPhone, addressee.phone);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
