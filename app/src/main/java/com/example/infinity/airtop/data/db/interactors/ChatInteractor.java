package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.ui.chat.ChatPresenter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides methods to {@link ChatPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class ChatInteractor extends BaseIntearctor{

    public String getNicknameById(String id){
        Future<String> future = service.submit(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            return addresseeDao.getNicknameById(id);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }

    public void insertMessage(Message message){
        service.submit(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            messageDao.insert(message);
        });
    }

    public void insertUser(User user){
        service.submit(()->{
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.insert(user);
        });
    }

    public ArrayList<Message> getAllMessagesByUUID(String uuid){
        Future<ArrayList<Message>> future = service.submit(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            return (ArrayList<Message>) messageDao.getMessagesByUUID(uuid);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }
}
