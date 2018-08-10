package com.example.infinity.airtop.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.di.components.ChatComponent;
import com.example.infinity.airtop.di.components.DaggerChatComponent;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Activity for sending and receiving messages
 *  @author infinity_coder
 *  @version 1.0.4
 */
public class ChatActivity extends MvpAppCompatActivity implements ChatView {



    @BindView(R.id.recyclerView)
    RecyclerView msgRecycler;
    @BindView(R.id.editText)
    EditText inputField;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ChatInteractor chatInteractor;
    @Inject
    ServerPostman serverPostman;
    @Inject
    MessageBus messageBus;
    @Inject
    MessageEditor messageEditor;
    @Inject
    AppPreferencesHelper preferencesHelper;


    private static final int LOAD_IMAGE_KEY = 1;
    private MessageRecyclerAdapter messageAdapter;
    private String addressId;
    private LinearLayoutManager layoutManager;
    private ChatComponent chatComponent;

    @InjectPresenter
    ChatPresenter presenter;

    @ProvidePresenter
    ChatPresenter providePresenter(){
        return chatComponent.getPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chatComponent = DaggerChatComponent.create();
        chatComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        if(savedInstanceState != null && savedInstanceState.getString(getResources().getString(R.string.intent_key_address_id)) != null)
            addressId = savedInstanceState.getString(getResources().getString(R.string.intent_key_address_id));
        else
            addressId = getIntent().getStringExtra(getResources().getString(R.string.intent_key_address_id));

        String senderId = App.getInstance().getCurrentUser().uuid;
        presenter.onCreate(addressId, senderId);

        // Set adapter AFTER restoring list of messages
        messageAdapter = new MessageRecyclerAdapter(addressId, chatInteractor);
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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE_KEY && resultCode == RESULT_OK && data != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                presenter.getMessageEditor().addImage(bitmap);
                Toast.makeText(this, getResources().getString(R.string.picture_attached), Toast.LENGTH_SHORT).show();
            } catch (IOException e) { e.printStackTrace(); }
        }

    }*/

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
        presenter.addTextToMessage(inputField.getText().toString());
        presenter.sendMessage();
        inputField.getText().clear();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(R.string.intent_key_address_id), addressId);
    }
}
