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
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.Message;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Activity for sending and receiving messages
 *  @author infinity_coder
 *  @version 1.0.3
 */

public class ChatActivity extends MvpAppCompatActivity implements ChatView {

    @InjectPresenter
    ChatPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView msgRecycler;
    @BindView(R.id.editText)
    EditText inputField;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final int LOAD_IMAGE_KEY = 1;
    private MessageRecyclerAdapter messageAdapter;
    private String addressId;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        addressId = getIntent().getStringExtra(getResources().getString(R.string.intent_key_address_id));
        String senderId = App.getInstance().getCurrentUser().uuid;
        presenter.onCreate(addressId, senderId);

        // Set adapter AFTER restoring list of messages
        messageAdapter = new MessageRecyclerAdapter(addressId);
        msgRecycler.setAdapter(messageAdapter);
        layoutManager = new LinearLayoutManager(this);
        msgRecycler.setLayoutManager(layoutManager);

        // It is refresh Recycler Adapter and set the last position after closing the activity
        msgRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int defaultPosition = messageAdapter.getItemCount() - 1;
                msgRecycler.scrollToPosition(presenter.getAdapterPosition(addressId, defaultPosition)); // TODO saving current position
                msgRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        toolbar.setTitle(presenter.getNickname());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE_KEY && resultCode == RESULT_OK && data != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                presenter.getMessageEditor().addImage(bitmap);
                Toast.makeText(this, getResources().getString(R.string.picture_attached), Toast.LENGTH_SHORT).show();
            } catch (IOException e) { e.printStackTrace(); }
        }

    }

    // After clicking on btnAffix app opens activity for choosing and affixing image
    @OnClick(R.id.btnAffix)
    void affixImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,
                getResources().getString(R.string.choose_image)), LOAD_IMAGE_KEY);
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
            presenter.getMessageEditor().addText(text);
            inputField.getText().clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.saveAdapterPosition(addressId, layoutManager.findLastVisibleItemPosition());
        presenter.onDestroy();
    }

    // Add message to adapter and display it
    @Override
    public void displayMessage(Message message){
        messageAdapter.addItem(message);
        messageAdapter.notifyDataSetChanged();
        msgRecycler.scrollToPosition(messageAdapter.getItemCount() - 1);
    }
}
