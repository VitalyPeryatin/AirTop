package com.infinity_coder.infinity.airtop.ui.auth.phone_verify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.infinity_coder.infinity.airtop.App;
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
import butterknife.OnClick;

/**
 *  Fragment for auth user by phone number and validate this phone number
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class PhoneVerifyFragment extends Fragment implements OnPhoneVerifyListener {

    private FirebaseAuth auth;
    private String verifId;
    private String phone;
    private AuthActivity parentActivity;
    private PhoneAuthComponent phoneAuthComponent;
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

    public PhoneVerifyFragment(){
        phoneAuthComponent = DaggerPhoneAuthComponent.create();
        phoneAuthComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verify, container, false);
        ButterKnife.bind(this, view);
        return view;
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
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Some code
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // Some code

                    }
                    // Show a message and update the UI
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    Log.d("mLogAuth", "onCodeSent()");

                    verifId = verificationId;
                }
            };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
    }

    @OnClick(R.id.btnSubmitCode)
    public void submitCode(){
        String code = etCodeVerify.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifId, code);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sgn in success, update UI wiith the signed-in user's information
                        Log.d("mLogAuth", "onComplete():Successful");
                        PhoneAuthRequest request = new PhoneAuthRequest(phone);
                        ServerConnection.getInstance().sendRequest(request.toJson());
                    } else {
                        Log.d("mLogAuth", "onComplete():Error");
                        // Sign in failed, display a message and update the UI
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
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
            new AppPreference(getContext()).saveCurrentPhone(phone);
            App.getInstance().updateCurrentUser();
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
