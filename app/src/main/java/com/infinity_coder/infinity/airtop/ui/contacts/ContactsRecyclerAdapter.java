package com.infinity_coder.infinity.airtop.ui.contacts;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ContactsInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactsViewHolder>
        implements OnContactListListener, Observer<Contact>{

    private HashMap<String, Contact> contactsWithUUID = new HashMap<>();
    private ArrayList<Contact> contacts = new ArrayList<>();

    private ContactsInteractor interactor = new ContactsInteractor();
    private Context context;
    private ContextMenuView contextMenuView;
    private boolean isActiveActionMode;

    ContactsRecyclerAdapter(ContactsFragment fragment, ContextMenuView contextMenuView, ContactUpgradeBus contactUpgradeBus){
        this.context = App.getInstance().getBaseContext();
        this.contextMenuView = contextMenuView;
        contactUpgradeBus.subscribe(fragment.getActivity(), this);

        ArrayList<String> addresseeArrayList = (ArrayList<String>) interactor.getUuidList();
        if(addresseeArrayList != null)
            for (String uuid : addresseeArrayList)
                interactor.getLiveAddresseeByUuid(uuid).observe(fragment, this);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_contact, viewGroup, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i) {
        contactsViewHolder.tvAddressTitle.setText(contacts.get(i).nickname);
        contactsViewHolder.tvLastMessage.setText(contacts.get(i).lastMessage);
        contactsViewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources() ,R.mipmap.default_avatar));
    }

    @Override
    public int getItemCount() {
        if (contactsWithUUID == null) return 0;
        return contactsWithUUID.size();
    }

    @Override
    public void addContact(Contact contact) {
        this.contactsWithUUID.put(contact.uuid, contact);
        this.contacts.add(contact);
        this.notifyDataSetChanged();
    }

    @Override
    public void removeContact(Contact contact) {
        contactsWithUUID.remove(contact.uuid);
        contacts.remove(contact);
        this.notifyDataSetChanged();
    }

    private void remove(Contact contact){
        this.contactsWithUUID.remove(contact.uuid);
        this.contacts.remove(contact);
        notifyDataSetChanged();
    }

    @Override
    public void onChanged(@Nullable Contact contact) {
        if(contact != null) {
            this.contactsWithUUID.put(contact.uuid, contact);
            this.contacts = new ArrayList<>(contactsWithUUID.values());
        }
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView tvAddressTitle, tvLastMessage;
        ImageView imageView;
        private FrameLayout container;
        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddressTitle = itemView.findViewById(R.id.addressTitle);
            tvLastMessage = itemView.findViewById(R.id.lastMsgText);
            imageView = itemView.findViewById(R.id.avatar_img);
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
            Intent intent = new Intent(context, ChatActivity.class);
            String addressId = context.getResources().getString(R.string.intent_key_address_id);
            intent.putExtra(addressId, contact.uuid);
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
                        Contact contact = contacts.get(getAdapterPosition());
                        // remove(contact);
                        interactor.deleteAddressWithMessages(contact);
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
