package com.example.infinity.airtop.service.client;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.Message;
import com.example.infinity.airtop.data.network.PhoneVerifier;
import com.example.infinity.airtop.data.network.RequestModel;
import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.client.writeData.ImageEncoder;
import com.example.infinity.airtop.service.client.writeData.TextEncoder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class JsonConverter {
    public String toJson(@NotNull RequestModel requestModel){
        Gson gson = new Gson();
        String jsonMessage = "";
        if(requestModel instanceof Message) {
            Message message = (Message) requestModel;
            if (message.getText() != null)
                new TextEncoder().encode(message);
            if (message.getImage() != null)
                new ImageEncoder().encode(message);
            jsonMessage = gson.toJson(message);
        }
        else if(requestModel instanceof UserRequest){
            UserRequest user = (UserRequest) requestModel;
            jsonMessage = gson.toJson(user);
        }
        else if(requestModel instanceof SearchableUsers){
            SearchableUsers searchableUsers = (SearchableUsers) requestModel;
            jsonMessage = gson.toJson(searchableUsers);
        }
        else if(requestModel instanceof PhoneVerifier){
            PhoneVerifier phoneVerifier = (PhoneVerifier) requestModel;
            jsonMessage = gson.toJson(phoneVerifier);
        }
        else if(requestModel instanceof CheckingUsername){
            CheckingUsername checkingUsername = (CheckingUsername) requestModel;
            jsonMessage = gson.toJson(checkingUsername);
        }
        jsonMessage = jsonMessage.length() + "@" + jsonMessage;
        return jsonMessage;
    }

}
