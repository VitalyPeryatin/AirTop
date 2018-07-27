package com.example.infinity.airtop.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.SearchUserPresenter;
import com.example.infinity.airtop.views.adapters.SearchUserAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchUserActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @BindView(R.id.recycler_users)
    RecyclerView recyclerUsers;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    private SearchUserPresenter presenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        unbinder = ButterKnife.bind(this);
        presenter = SearchUserPresenter.getInstance();
        presenter.attachActivity(this);

        setSupportActionBar(toolbar);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.setAdapter(new SearchUserAdapter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public SearchUserAdapter getSearchAdapter() {
        return (SearchUserAdapter) recyclerUsers.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.length() >= 5)
            presenter.sendSearchableString(s);
        else
            getSearchAdapter().clear();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
