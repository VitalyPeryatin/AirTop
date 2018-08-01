package com.example.infinity.airtop.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.Message;
import com.example.infinity.airtop.ui.adapters.MessageListViewAdapter;

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

public class ChatActivity extends MvpAppCompatActivity implements ChatView {

    @InjectPresenter
    ChatPresenter presenter;

    private static final int LOAD_IMAGE_CODE = 1;

    // TODO Отрефакторить код срочно!!!
    @BindView(R.id.recyclerView)
    RecyclerView msgListView;
    @BindView(R.id.editText)
    EditText inputField;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private MessageListViewAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        presenter.onCreate(this, getIntent());

        // Set adapter AFTER restoring list of messages
        ArrayList<Message> list = new ArrayList<>(); // TODO Внутри list должны лежать сообщения из БД
        messageAdapter = new MessageListViewAdapter(list);
        msgListView.setAdapter(messageAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitle(presenter.getAddresseeUserPhone());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE_CODE && resultCode == RESULT_OK && data != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                presenter.addImageToMsg(bitmap);
                Toast.makeText(this, "Картинка прикреплена", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void sendMessage(){
        String text = inputField.getText().toString();
        presenter.addTextToMsg(text);
        presenter.sendMessage();
        inputField.getText().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.closeConnection();
        presenter.onDestroy();
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void displayMessage(Message message){
        runOnUiThread(()-> {
            messageAdapter.addItem(message);
            messageAdapter.notifyDataSetChanged();
            msgListView.scrollToPosition(messageAdapter.getItemCount() - 1);
        });
    }
}
