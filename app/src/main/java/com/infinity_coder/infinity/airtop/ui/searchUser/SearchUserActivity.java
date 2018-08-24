package com.infinity_coder.infinity.airtop.ui.searchUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.network.response.SearchUserResponse;
import com.infinity_coder.infinity.airtop.utils.decorations.SearchUserHorizontalLineItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchUserActivity extends MvpAppCompatActivity implements SearchView.OnQueryTextListener, SearchUserView{

    @BindView(R.id.recycler_users)
    RecyclerView recyclerUsers;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    @InjectPresenter
    public SearchUserPresenter presenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        unbinder = ButterKnife.bind(this);
        presenter.onCreate();

        setSupportActionBar(toolbar);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.addItemDecoration(new SearchUserHorizontalLineItemDecoration(this, R.drawable.divider_horizontal));
        recyclerUsers.setAdapter(new SearchUserAdapter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private SearchUserAdapter getSearchAdapter() {
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
            presenter.sendSearchableUsername(s);
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
        presenter.onDestroy();
    }

    @Override
    public void displaySearchableUsers(SearchUserResponse response) {
        runOnUiThread(() -> getSearchAdapter().updateList(response.getUsers()));

    }
}
