package com.example.infinity.airtop.ui.auth.entry_code;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.ui.auth.AuthActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryAuthFragment extends Fragment {

    @BindView(R.id.etSecretCode)
    EditText editTextSecretCode;

    // Show when user already have written entry-code
    private static final String SECRET_CODE = "road12345";
    private AuthActivity parentActivity;
    private AuthPreference sPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
        sPref = new AuthPreference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_code, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnSendCode)
    void click(){
        String code = editTextSecretCode.getText().toString();
        if (code.equals(SECRET_CODE)) {
            sPref.saveEnter(true);
            parentActivity.changeView();
        } else {
            editTextSecretCode.setText("");
        }
    }
}
