package com.example.infinity.airtop.ui.contacts;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ContactsInteractor;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.network.request.SubscribeUserUpdateRequest;
import com.example.infinity.airtop.ui.chat.ChatActivity;
import com.example.infinity.airtop.utils.ServerPostman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactsViewHolder>
        implements OnContactListListener, Observer<List<Addressee>>{

    private HashMap<String, Contact> contactsWithUUID = new HashMap<>();
    private ArrayList<Contact> contacts = new ArrayList<>();

    private ContactsInteractor interactor = new ContactsInteractor();
    private Context context;
    private ContextMenuView contextMenuView;
    private boolean isActiveActionMode;

    ContactsRecyclerAdapter(FragmentActivity activity, ContextMenuView contextMenuView){
        this.context = App.getInstance().getBaseContext();
        this.contextMenuView = contextMenuView;
        interactor.getLiveAddress().observe(activity, this);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_contact, viewGroup, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i) {
        contactsViewHolder.tvAddressTitle.setText(contacts.get(i).addressee.username);
        contactsViewHolder.tvLastMessage.setText(contacts.get(i).lastMessage);
    }

    @Override
    public int getItemCount() {
        if (contactsWithUUID == null) return 0;
        return contactsWithUUID.size();
    }

    @Override
    public void onUpdateContact(String uuid, Contact contact) {
        this.contactsWithUUID.put(uuid, contact);
        this.contacts = new ArrayList<>(contactsWithUUID.values());
        notifyDataSetChanged();
    }

    @Override
    public void onUpdateContact(String uuid, String lastMessage) {
        this.contactsWithUUID.get(uuid).lastMessage = lastMessage;
        notifyDataSetChanged();
    }

    @Override
    public void onChanged(@Nullable List<Addressee> addressees) {
        this.contactsWithUUID = interactor.getUpdatedContacts(addressees);
        ArrayList<String> uuids = new ArrayList<>(this.contactsWithUUID.keySet());
        new ServerPostman().postRequest(new SubscribeUserUpdateRequest(uuids));
        this.contacts = new ArrayList<>(contactsWithUUID.values());
        notifyDataSetChanged();
    }


    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView tvAddressTitle, tvLastMessage;
        private LinearLayout container;
        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddressTitle = itemView.findViewById(R.id.addressTitle);
            tvLastMessage = itemView.findViewById(R.id.lastMsgText);
            container = itemView.findViewById(R.id.container);

            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.container:
                    // As a parameter is passed the Contact which clicked by the user
                    startChatActivity(contacts.get(getAdapterPosition()));
                    break;
            }
        }

        private void startChatActivity(Contact contact){
            Addressee addressee = contact.addressee;
            Intent intent = new Intent(context, ChatActivity.class);
            String addressId = context.getResources().getString(R.string.intent_key_address_id);
            intent.putExtra(addressId, addressee.uuid);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.container:
                    if(!isActiveActionMode)
                        contextMenuView.showMenu(actionModeCallback);
                    break;
            }
            return true;
        }

        private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.contacts_menu, menu);
                isActiveActionMode = true;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_delete:
                        Addressee addressee = contacts.get(getAdapterPosition()).addressee;
                        interactor.deleteAddressWithMessages(addressee);
                        notifyDataSetChanged();
                        actionMode.finish();
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                isActiveActionMode = false;
            }
        };
    }
}
