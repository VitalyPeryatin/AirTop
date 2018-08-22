package com.infinity_coder.infinity.airtop.ui.auth.phone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
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

    private AuthActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_phone_auth, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
    }

    @OnClick(R.id.btnAuth)
    public void auth(){
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
