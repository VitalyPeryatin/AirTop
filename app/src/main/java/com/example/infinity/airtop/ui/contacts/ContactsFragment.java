package com.example.infinity.airtop.ui.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.ui.searchUser.SearchUserActivity;
import com.melnykov.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *  Fragment shows list of last companions
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class ContactsFragment extends MvpAppCompatFragment implements ContextMenuView{

    @BindView(R.id.recycler_contacts)
    RecyclerView recyclerContacts;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private ContactsRecyclerAdapter adapter;
    private ContactUpgradeBus contactUpgradeBus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, layout);
        contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
        assert getActivity() != null;
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_title_main);
        adapter = new ContactsRecyclerAdapter(this);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContacts.setAdapter(adapter);

        fab.attachToRecyclerView(recyclerContacts);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        contactUpgradeBus.subscribe(adapter);
        adapter.onLoadContacts();
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

    @Override
    public void onStop() {
        super.onStop();
        contactUpgradeBus.unsubscribe();
    }

    @Override
    public void showMenu(ActionMode.Callback actionModeCallback) {
        toolbar.startActionMode(actionModeCallback);
    }
}
