package com.example.infinity.airtop.utils;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.data.network.PhoneVerifier;
import com.example.infinity.airtop.data.network.RequestModel;
import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.client.writeData.ImageEncoder;
import com.example.infinity.airtop.service.client.writeData.TextEncoder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

/**
 * Converting JSON and RequestModel objects
 * @author infinity_coder
 * @version 1.0.2
 */
public class JsonConverter {
    public String toJson(@NotNull RequestModel requestModel){
        Gson gson = new Gson();
        String jsonMessage = "";
        if(requestModel instanceof MessageRequest) {
            MessageRequest messageRequest = (MessageRequest) requestModel;
            if (messageRequest.getText() != null)
                new TextEncoder().encode(messageRequest);
            if (messageRequest.getImage() != null)
                new ImageEncoder().encode(messageRequest);
            jsonMessage = gson.toJson(messageRequest);
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
