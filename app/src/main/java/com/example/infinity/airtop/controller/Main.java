package com.example.infinity.airtop.controller;

import com.example.infinity.airtop.controller.client.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final Client client = new Client();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!client.isQuit()){
                    if(client.getOutMsgQueue().size() > 0)
                        System.out.println(client.getOutMsgQueue().poll());
                }
            }
        }).start();
        while (!client.isQuit()){}
        client.close();



    }


}
