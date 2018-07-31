package com.example.infinity.airtop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.PhoneVerifier;
import com.example.infinity.airtop.models.RequestModel;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.models.databases.DatabaseHandler;
import com.example.infinity.airtop.models.databases.UserDao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Unbinder unbinder;
    private static final int LOGIN_CODE = 2;
    private static final String USER_PHONE_KEY = "user_phone_key";
    private String currentUserPhone = null;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_activity);
        unbinder = ButterKnife.bind(this);
        sPref = getSharedPreferences("savedUserPhone", MODE_PRIVATE);

        setFragment(new ContactsFragment());
        navigationView.setNavigationItemSelectedListener(this);

        currentUserPhone = loadCurrentUser();

        if(currentUserPhone == null ){
            showLoginActivity();
        }
    }


    public void showLoginActivity(){
        startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_CODE && resultCode == RESULT_OK && data != null){
            Log.d("mLog", "Регистрация прошла успешно");
            currentUserPhone = data.getStringExtra("user phone");
            App.getInstance().verifyUser();
        }
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void saveCurrentUser(){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(USER_PHONE_KEY, currentUserPhone);
        editor.apply();
    }

    private String loadCurrentUser(){
        return sPref.getString(USER_PHONE_KEY, null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item_settings:
                Intent intent = new Intent(NavDrawerActivity.this, SettingsActivity.class);
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
