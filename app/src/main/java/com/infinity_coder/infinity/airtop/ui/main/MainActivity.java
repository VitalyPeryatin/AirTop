package com.infinity_coder.infinity.airtop.ui.main;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.ui.auth.AuthActivity;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsActivity;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity for controlling fragment by navigation drawer,
 * show ContactsFragment and verify existing users.
 * @author infinity_coder
 * @version 1.0.4
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer<User>{

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Unbinder unbinder;
    private static final int AUTH_REQUEST_CODE = 1;
    private TextView tvName;
    private TextView tvPhone;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_activity);
        unbinder = ButterKnife.bind(this);

        setFragment(new ContactsFragment());
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvHeaderName);
        tvPhone = headerView.findViewById(R.id.tvHeaderPhone);

        currentUser = App.getInstance().getCurrentUser();
        if(currentUser == null)
            showLoginActivity();
        else {
            setHeaderUserInfo();
        }
    }

    private void setHeaderUserInfo(){
        App.getInstance().getCurrentLiveUser().observe(this, this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                setHeaderUserInfo();
            }
            else{
                finish();
            }
        }
    }

    public void showLoginActivity(){
        startActivityForResult(new Intent(this, AuthActivity.class), AUTH_REQUEST_CODE);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onChanged(@Nullable User user) {
        tvName.setText(user.nickname);
        tvPhone.setText(user.phone);
    }
}
