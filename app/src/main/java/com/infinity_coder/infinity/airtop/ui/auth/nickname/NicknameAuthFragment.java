package com.infinity_coder.infinity.airtop.ui.auth.nickname;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NicknameAuthFragment extends Fragment implements OnNicknameAuthListener{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.toolbar_nickname)
    Toolbar toolbar;

    private AuthActivity parentActivity;
    private AuthPreference sPref;
    private ChatInteractor interactor;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getResponseListeners().getNicknameAuthBus().subscribe(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_nickname_auth, container, false);
        parentActivity = (AuthActivity) getActivity();
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setToolbar(toolbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        context = getContext();
        interactor = new ChatInteractor();
        sPref = new AuthPreference(context);
        super.onActivityCreated(savedInstanceState);
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
        switch (item.getItemId()){
            case R.id.item_apply:
                auth();
                break;
        }
        return true;
    }

    private void auth(){
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        if(firstName.length() >= 3) {
            String nickname = (firstName + " " + lastName).trim();
            String phone = sPref.getCurrentPhone();
            NicknameAuthRequest request = new NicknameAuthRequest(phone, nickname);
            ServerConnection.getInstance().sendRequest(request);
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
            new AppPreference(context).saveCurrentPhone(user.phone);
            parentActivity.changeView();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getNicknameAuthBus().unsubscribe(this);
    }
}
