package com.example.infinity.airtop.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.ui.chat.ChatActivity;

import java.util.ArrayList;

public class ContactsListViewAdapter extends RecyclerView.Adapter<ContactsListViewAdapter.ContactsViewHolder> {

    private ArrayList<Addressee> contacts = new ArrayList<>();
    private Fragment fragment;

    public ContactsListViewAdapter(Fragment fragment){
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
        contactsViewHolder.textView.setText(contacts.get(i).username);
    }

    public void onUpdateList(){
        new Thread(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            contacts = (ArrayList<Addressee>) addresseeDao.getAll();
            fragment.getActivity().runOnUiThread(() -> notifyDataSetChanged());
        }).start();
    }

    public void addAddressee(Addressee addressee){
        contacts.add(addressee);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text);
            textView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.text:
                    Addressee addressee = contacts.get(getAdapterPosition());
                    Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
                    intent.putExtra("addresseePhone", addressee.phone);

                    fragment.getActivity().runOnUiThread(() -> {
                        fragment.getActivity().startActivity(intent);
                    });
                    break;
            }
        }
    }
}
