package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.data.network.UserRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChatInteractor {
    private ExecutorService service = Executors.newCachedThreadPool();

    public Addressee getAddresseeByPhone(String phone){
        Future<Addressee> future = service.submit(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            return addresseeDao.getByPhone(phone);
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
}
