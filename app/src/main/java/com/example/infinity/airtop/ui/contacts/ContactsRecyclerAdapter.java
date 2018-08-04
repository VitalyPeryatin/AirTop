package com.example.infinity.airtop.ui.contacts;

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
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactsViewHolder> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private Fragment fragment;

    public ContactsRecyclerAdapter(Fragment fragment){
        this.fragment = fragment;
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
        new Thread(() -> {
            contacts.clear();
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Addressee> addressees = (ArrayList<Addressee>) addresseeDao.getAll();
            for (Addressee addressee : addressees) {
                Contact contact = new Contact();
                contact.setAddressee(addressee);
                Message message = messageDao.getLastMessageByAddressePhone(addressee.phone);
                contact.setLastMessage(message.text);
                contacts.add(contact);
            }
            fragment.getActivity().runOnUiThread(() -> notifyDataSetChanged());
        }).start();
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
                    Intent intent = new Intent(App.getInstance().getBaseContext(), ChatActivity.class);
                    intent.putExtra("addresseePhone", addressee.phone);
                    App.getInstance().getBaseContext().startActivity(intent);
                    break;
            }
        }
    }
}
