package com.infinity_coder.infinity.airtop.data.db.interactors;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Addressee;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.UserDao;
import com.infinity_coder.infinity.airtop.data.network.request.AddressRequest;
import com.infinity_coder.infinity.airtop.data.network.response.MessageResponse;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.chat.ChatPresenter;

import java.util.ArrayList;
import java.util.concurrent.Callable;
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

    private Callable insertMessageCallable;
    private final Object lock = new Object();
    private App app = App.getInstance();
    private static boolean isUpdatedMessage = true;


    public String getNicknameById(String id){
        Future<String> future = service.submit(() -> {
            AddresseeDao addresseeDao = app.getDatabase().addresseeDao();
            return addresseeDao.getNicknameById(id);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }

    public String getUsernameById(String id){
        Future<String> future = service.submit(() -> {
            AddresseeDao addresseeDao = app.getDatabase().addresseeDao();
            return addresseeDao.getUsernameById(id);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }

    public void insertMessage(Message message){
        String uuid = message.addressId;

        Future future = service.submit(() -> {
            if (getNicknameById(uuid) == null) {
                Addressee addressee = new Addressee(uuid);
                AddresseeDao addresseeDao = app.getDatabase().addresseeDao();
                addresseeDao.insert(addressee);
                Contact contact = new Contact(addressee, message.text);
                MessageDao messageDao = app.getDatabase().messageDao();
                messageDao.insert(message);
                app.getResponseListeners().getContactUpgradeBus().onUpdateLastMessage(uuid, contact);
            }
            else {
                MessageDao messageDao = app.getDatabase().messageDao();
                messageDao.insert(message);
                app.getResponseListeners().getContactUpgradeBus().onUpdateLastMessage(uuid, message.text);
            }
            return null;
        });

        try {
            future.get(1, TimeUnit.SECONDS);
            if(getUsernameById(uuid) == null){
                AddressRequest request = new AddressRequest(uuid);
                ServerConnection.getInstance().sendRequest(request.toJson());
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    public void insertAddressee(Addressee addressee) {
        service.submit(() -> {
            AddresseeDao addresseeDao = app.getDatabase().addresseeDao();
            addresseeDao.insert(addressee);
        });
    }

    public void insertMessage(MessageResponse message){
        insertMessage(message.toMessageModel());
    }

    public void insertUser(User user){
        service.submit(()->{
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.insert(user);
        });
        App.getInstance().updateCurrentUser();
    }

    public ArrayList<Message> getAllMessagesByUUID(String uuid){
        Future<ArrayList<Message>> future = service.submit(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            return (ArrayList<Message>) messageDao.getMessagesByUUID(uuid);
        });
        try { return future.get(5, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }
}
