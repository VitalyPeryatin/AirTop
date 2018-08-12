package com.example.infinity.airtop.ui.auth.phone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.di.components.DaggerPhoneAuthComponent;
import com.example.infinity.airtop.di.components.PhoneAuthComponent;
import com.example.infinity.airtop.ui.auth.AuthActivity;
import com.example.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Fragment for auth user by phone number and validate this phone number
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class PhoneAuthFragment extends MvpAppCompatFragment implements PhoneAuthView {

    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;

    @Inject
    ChatInteractor interactor;
    @Inject
    ServerPostman serverPostman;
    @Inject
    PhoneAuthBus phoneAuthBus;
    @Inject
    AuthPreference authPreference;

    @InjectPresenter
    PhoneAuthPresenter presenter;
    @ProvidePresenter
    PhoneAuthPresenter providePresenter(){
        return phoneAuthComponent.providePhoneAuthPresenter();
    }

    private AuthActivity parentActivity;
    private PhoneAuthComponent phoneAuthComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        phoneAuthComponent = DaggerPhoneAuthComponent.create();
        phoneAuthComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_phone_auth, container, false);
        ButterKnife.bind(this, view);
        presenter.onCreate();
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
        presenter.sendPhone(phone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void notValidPhone() {
        editTextAuth.getText().clear();
    }

    @Override
    public void successfulPhoneAuth() {
        parentActivity.changeView();
    }
}
