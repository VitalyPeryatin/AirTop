package com.example.infinity.airtop.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.views.adapters.ContactsListViewAdapter;
import com.melnykov.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactsFragment extends Fragment {

    @BindView(R.id.recycler_contacts)
    RecyclerView recyclerContacts;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    ContactsListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, layout);

        toolbar.setTitle("Чаты");
        adapter = new ContactsListViewAdapter();
        fillAdapter();
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContacts.setAdapter(adapter);

        fab.attachToRecyclerView(recyclerContacts);


        return layout;
    }

    @OnClick(R.id.fab)
    void searchNewUser(){
        startActivity(new Intent(getContext(), SearchUserActivity.class));
    }

    private void fillAdapter(){
        adapter.addContact("Петя");
        adapter.addContact("Вася");
        adapter.addContact("Коля");
        adapter.addContact("Маша");
        adapter.addContact("Виталя");
        adapter.addContact("Наташа");
        adapter.addContact("Лёша");
        adapter.addContact("Артём");
        adapter.addContact("Марина");
        adapter.addContact("Кристина");
        adapter.addContact("Костя");
        adapter.addContact("Андрей");
        adapter.addContact("Кирилл");
        adapter.addContact("Катя");
        adapter.addContact("Соня");
        adapter.addContact("Рената");
        adapter.addContact("Павел");
        adapter.addContact("Петя");
        adapter.addContact("Вася");
        adapter.addContact("Коля");
        adapter.addContact("Маша");
        adapter.addContact("Виталя");
        adapter.addContact("Наташа");
        adapter.addContact("Лёша");
        adapter.addContact("Артём");
        adapter.addContact("Марина");
        adapter.addContact("Кристина");
        adapter.addContact("Костя");
        adapter.addContact("Андрей");
        adapter.addContact("Кирилл");
        adapter.addContact("Катя");
        adapter.addContact("Соня");
        adapter.addContact("Рената");
        adapter.addContact("Павел");

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
