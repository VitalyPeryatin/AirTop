package com.example.infinity.airtop.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.controller.client.Client;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button btnSend;
    public RecyclerView recyclerView;
    private Client client;
    private ArrayList currentMessages = new ArrayList<>();
    private Gson gson = new Gson();

    private static final String CURR_MESSAGE_KEY = "currentMessages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        connectToServer();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                client.getInMsgQueue().add(text);
                editText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        recyclerView.setAdapter(new RecyclerViewAdapter(currentMessages));
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String mes;
        if((mes = savedInstanceState.getString(CURR_MESSAGE_KEY)) != null){
            currentMessages = gson.fromJson(mes, ArrayList.class);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String mes = gson.toJson(currentMessages);
        outState.putString(CURR_MESSAGE_KEY, mes);
    }

    private void connectToServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Client(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
