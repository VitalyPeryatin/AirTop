package com.infinity_coder.infinity.airtop.ui.auth.phone_verify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.network.request.PhoneAuthRequest;
import com.infinity_coder.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.di.components.DaggerPhoneAuthComponent;
import com.infinity_coder.infinity.airtop.di.components.PhoneAuthComponent;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.auth.AuthActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  Fragment for auth user by phone number and validate this phone number
 *  @author infinity_coder
 *  @version 1.0.5
 */
public class PhoneVerifyFragment extends Fragment implements OnPhoneVerifyListener {

    private FirebaseAuth auth;
    private String verifId;
    private String phone;
    private AuthActivity parentActivity;
    @Inject
    ChatInteractor interactor;
    @Inject
    PhoneVerifyBus phoneAuthBus;
    @Inject
    AuthPreference authPref;
    @Inject
    AuthPreference appPref;

    @BindView(R.id.etCodeVerify)
    EditText etCodeVerify;
    @BindView(R.id.toolbar_phone_verify)
    Toolbar toolbar;

    public PhoneVerifyFragment(){
        PhoneAuthComponent phoneAuthComponent = DaggerPhoneAuthComponent.create();
        phoneAuthComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verify, container, false);
        parentActivity = (AuthActivity) getActivity();
        ButterKnife.bind(this, view);
        setToolbar(toolbar);
        return view;
    }

    private void setToolbar(Toolbar toolbar){
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
        switch (item.getItemId()){
            case R.id.item_apply:
                submitCode();
                break;
        }
        return true;
    }

    private void submitCode(){
        try {
            String code = etCodeVerify.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifId, code);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast.makeText(getContext(), "Неверный код", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null && getActivity() != null) {
            phone = getArguments().getString("phoneNumber", "");

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    60,
                    TimeUnit.SECONDS,
                    getActivity(),
                    mCallbacks
            );
        }
        phoneAuthBus.subscribe(this);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d("mLogAuth", "onVerificationCompleted()");
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.d("mLogAuth", "onVerificationFailed()");
                    e.printStackTrace();
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    Log.d("mLogAuth", "onCodeSent()");

                    verifId = verificationId;
                }
            };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sgn in success, update UI wiith the signed-in user's information
                        Log.d("mLogAuth", "onComplete():Successful");
                        PhoneAuthRequest request = new PhoneAuthRequest(phone);
                        ServerConnection.getInstance().sendRequest(request);
                    }
                });
    }

    @Override
    public void onPhoneVerify(PhoneAuthResponse response) {
        if(response.getResult().equals("RESULT_OK")){
            authPref.saveCurrentPhone(phone);
        }
        else if (response.getResult().equals("RESULT_EXISTS")){
            authPref.saveCurrentPhone(phone);
            authPref.saveUserHasNickname(true);
            if(getContext() != null) {
                new AppPreference(getContext()).saveCurrentPhone(phone);
            }
            interactor.insertUser(response.getUser());
        }
        parentActivity.changeView();
    }

    @Override
    public void onStop() {
        super.onStop();
        phoneAuthBus.subscribe(this);
    }
}
