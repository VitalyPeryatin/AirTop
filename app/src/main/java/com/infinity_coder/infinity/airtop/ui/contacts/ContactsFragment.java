package com.infinity_coder.infinity.airtop.ui.contacts;

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
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.ui.main.MainActivity;
import com.infinity_coder.infinity.airtop.ui.searchUser.SearchUserActivity;
import com.infinity_coder.infinity.airtop.utils.decorations.ContactHorizontalLineItemDecoration;
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
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, layout);
        ContactUpgradeBus contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
        activity = (MainActivity) getActivity();

        setToolbar(toolbar);

        ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(this, this, contactUpgradeBus);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContacts.addItemDecoration(new ContactHorizontalLineItemDecoration(activity, R.drawable.divider_horizontal));
        recyclerContacts.setAdapter(adapter);

        fab.attachToRecyclerView(recyclerContacts);
        return layout;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_title_main);
        toolbar.setNavigationIcon(R.drawable.menu_burger);
        toolbar.setNavigationOnClickListener(v -> activity.openDrawer());
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
    public void showMenu(ActionMode.Callback actionModeCallback) {
        toolbar.startActionMode(actionModeCallback);
    }
}
