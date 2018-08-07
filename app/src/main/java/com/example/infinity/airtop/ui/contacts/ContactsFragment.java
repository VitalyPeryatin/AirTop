package com.example.infinity.airtop.ui.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.ui.searchUser.SearchUserActivity;
import com.melnykov.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactsFragment extends MvpAppCompatFragment implements ContactsView {

    @InjectPresenter
    ContactsPresenter presenter;

    @BindView(R.id.recycler_contacts)
    RecyclerView recyclerContacts;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Unbinder unbinder;
    ContactsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, layout);

        assert getActivity() != null;
        AppCompatActivity parentActivity = (AppCompatActivity)getActivity();
        parentActivity.setSupportActionBar(toolbar);
        toolbar.setTitle("Чаты");
        adapter = new ContactsRecyclerAdapter(toolbar);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContacts.setAdapter(adapter);

        fab.attachToRecyclerView(recyclerContacts);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.onUpdateList();
    }

    @OnClick(R.id.fab)
    void searchNewUser(){
        startActivity(new Intent(getContext(), SearchUserActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
