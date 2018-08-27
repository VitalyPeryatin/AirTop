package com.infinity_coder.infinity.airtop.ui.auth.phone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.ui.auth.AuthActivity;
import com.infinity_coder.infinity.airtop.ui.auth.phone_verify.PhoneVerifyFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Fragment for auth user by phone number and validate this phone number
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class PhoneAuthFragment extends Fragment {

    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;
    @BindView(R.id.toolbar_user_phone)
    Toolbar toolbar;

    private AuthActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_phone_auth, container, false);
        parentActivity = (AuthActivity) getActivity();
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setToolbar(toolbar);
        return view;
    }

    private void setToolbar(Toolbar toolbar) {
        parentActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(view -> parentActivity.onBackPressed());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_apply, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_apply:
                auth();
                break;
        }
        return true;
    }

    private void auth(){
        String phone = editTextAuth.getText().toString();

        if(isValidPhone(phone)) {
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phone);
            Fragment phoneVerifyFragment = new PhoneVerifyFragment();
            phoneVerifyFragment.setArguments(bundle);

            parentActivity.addFragmentToBackstack(phoneVerifyFragment);
        }
        else{
            editTextAuth.getText().clear();
        }
    }

    private boolean isValidPhone(String phoneNumber){
        phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        Matcher matcher = Pattern.compile("(\\+7)([0-9]){10}").matcher(phoneNumber);
        return matcher.matches();
    }
}
