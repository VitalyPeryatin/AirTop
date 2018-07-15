package com.example.infinity.airtop.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.controller.client.BackendClient;
import com.example.infinity.airtop.model.Message;
import com.example.infinity.airtop.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Activity for sending and receiving messages
 *  @author infinity_coder
 *  @version 1.0.0
 */
public class ChatActivity extends AppCompatActivity {
    private static final int LOAD_IMAGE_CODE = 1;
    private static final int LOGIN_CODE = 2;

    // TODO Отрефакторить код срочно!!!
    @BindView(R.id.recyclerView)
    RecyclerView msgListView;
    @BindView(R.id.editText)
    EditText inputField;
    private BackendClient backendClient;
    private Gson gson = new Gson();
    private Message message;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (user == null)
            createUser();
        ButterKnife.bind(this);
        message = new Message();
        backendClient = new BackendClient(this);
    }

    private void createUser(){
        startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_CODE);
    }

    public RecyclerView getMsgListView() {
        return msgListView;
    }

    @Override
    protected void onResume() {
        // Set adapter AFTER restoring list of messages
        ArrayList<Message> list = new ArrayList<>(); // TODO Внутри list должны лежать сообщения из БД
        msgListView.setAdapter(new MessageListViewAdapter(list));
        msgListView.setLayoutManager(new LinearLayoutManager(this));
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE_CODE && resultCode == RESULT_OK && data != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                message.setImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(requestCode == LOGIN_CODE && resultCode == RESULT_OK && data != null){
            String jsonUser = data.getStringExtra("user");
            Gson gson = new Gson();
            user = gson.fromJson(jsonUser, User.class);
        }
    }

    @OnClick(R.id.btnAffix)
    void affixImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), 1);
    }

    @OnClick(R.id.btnSend)
    void sendMessage(){
        String text = inputField.getText().toString();
        if(text.length()>0) {
            message.setText(text);
            backendClient.sendMessage(message);
            inputField.getText().clear();
        }
        message = new Message();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(backendClient != null) {
            backendClient.closeConnection();
            backendClient = null;
        }
    }
}
