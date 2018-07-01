package com.example.infinity.airtop.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTest {
    public static void main(String[] args) {
        DataObject dataObj = new DataObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dataObj);
        System.out.println(json);
    }
}

class DataObject{
    String text;


    DataObject(){

    }
}
