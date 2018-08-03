package com.example.infinity.airtop.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.settings.SettingsActivity;
import com.example.infinity.airtop.ui.contacts.ContactsFragment;
import com.example.infinity.airtop.ui.auth.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Unbinder unbinder;
    private static final int LOGIN_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_activity);
        unbinder = ButterKnife.bind(this);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        setFragment(new ContactsFragment());
        navigationView.setNavigationItemSelectedListener(this);

        UserRequest currentUser = App.getInstance().getCurrentUser();
        if(currentUser == null ){
            showLoginActivity();
        }
        else {
            App.getInstance().verifyUserPhone();
        }
    }


    public void showLoginActivity(){
        startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_CODE && resultCode == RESULT_OK && data != null){
            App.getInstance().verifyUserPhone();
        }
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
}
