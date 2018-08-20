package com.infinity_coder.infinity.airtop.ui.auth.nickname;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.network.request.NicknameAuthRequest;
import com.infinity_coder.infinity.airtop.data.network.response.NicknameAuthResponse;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.auth.AuthActivity;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NicknameAuthFragment extends Fragment implements OnNicknameAuthListener{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    ServerPostman serverPostman;

    private AuthActivity parentActivity;
    private AuthPreference sPref;
    private ChatInteractor interactor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_nickname_auth, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getResponseListeners().getNicknameAuthBus().subscribe(this);
        serverPostman = new ServerPostman();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
        sPref = new AuthPreference(getContext());
        interactor = new ChatInteractor();
    }

    @OnClick(R.id.btnNicknameAuth)
    public void auth(){
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        if(firstName.length() >= 3) {
            String nickname = (firstName + " " + lastName).trim();
            String phone = sPref.getCurrentPhone();
            NicknameAuthRequest request = new NicknameAuthRequest(phone, nickname);
            ServerConnection.getInstance().sendRequest(request.toJson());
        }
        else{
            Toast.makeText(parentActivity, "Короткое имя", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNicknameAuth(NicknameAuthResponse response) {
        if(response != null) {
            User user = new User(response);
            interactor.insertUser(user);
            sPref.saveUserHasNickname(true);
            new AppPreference(getContext()).saveCurrentPhone(user.phone);
            App.getInstance().updateCurrentUser();
            parentActivity.changeView();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getNicknameAuthBus().unsubscribe(this);
    }
}
