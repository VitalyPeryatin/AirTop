package com.example.infinity.airtop.ui.auth.nickname;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.response.NicknameAuthResponse;
import com.example.infinity.airtop.data.network.request.NicknameAuthRequest;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.ui.auth.AuthActivity;
import com.example.infinity.airtop.utils.JsonConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NicknameAuthFragment extends Fragment implements OnNicknameAuthListener{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;

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
        App.getInstance().getResponseListeners().getNicknameAuthListener().subscribe(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
        sPref = new AuthPreference();
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

            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(request);
            Intent intent = new Intent(parentActivity, ClientService.class);
            intent.putExtra("request", json);
            parentActivity.startService(intent);
            parentActivity.changeView();
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
            sPref.saveHaveNickname(true);
            new AppPreference().saveCurrentPhone(user.phone);
            App.getInstance().updateCurrentUser();
            parentActivity.changeView();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getNicknameAuthListener().unsubscribe(this);
    }
}
