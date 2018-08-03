package com.example.infinity.airtop.ui.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.ui.adapters.MessageListViewAdapter;

import java.io.IOException;

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

    @BindView(R.id.recyclerView)
    RecyclerView msgListView;
    @BindView(R.id.editText)
    EditText inputField;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final int LOAD_IMAGE_CODE = 1;
    private MessageListViewAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        presenter.onCreate(getIntent());

        // Set adapter AFTER restoring list of messages
        messageAdapter = new MessageListViewAdapter(presenter.getAddresseeUserPhone());
        msgListView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgListView.setLayoutManager(layoutManager);

        msgListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                msgListView.scrollToPosition(messageAdapter.getItemCount() - 1);
                msgListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


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

    // After clicking on btnAffix app opens activity for choosing and affixing image
    @OnClick(R.id.btnAffix)
    void affixImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), LOAD_IMAGE_CODE);
    }

    @OnClick(R.id.btnSend)
    public void sendMessage() {
        addTextToMessage();
        presenter.sendMessage();
    }

    // Add text to message if text is exists
    private void addTextToMessage(){
        String text = inputField.getText().toString();
        if(text.length() > 0) {
            presenter.addTextToMsg(text);
            inputField.getText().clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void displayMessage(MessageRequest messageRequest){
        messageAdapter.addItem(messageRequest);
        messageAdapter.notifyDataSetChanged();
        msgListView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }
}
