package com.infinity_coder.infinity.airtop.data.db.interactors;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.AppDatabase;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.ContactDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.UserDao;
import com.infinity_coder.infinity.airtop.data.network.request.AddressRequest;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.chat.ChatPresenter;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactUpgradeBus;

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
public class ChatInteractor extends BaseInteractor {

    private AppDatabase database = App.getInstance().getDatabase();
    private ContactsInteractor contactsInteractor = new ContactsInteractor();

    private String getUsernameById(String id){
        Future<String> future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            return contactDao.getUsernameById(id);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }

    public void insertMessage(String nickname, Message message){
        String uuid = message.addressId;

        Future future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            if (contactsInteractor.getNicknameById(uuid) == null) {
                Contact contact = new Contact(uuid, nickname);
                contactDao.insert(contact);
            }
            Contact contact = contactDao.getContactById(uuid);
            if(message.text != null)
                contact.lastMessage = message.text;
            else{
                if(message.imagePath != null)
                    contact.lastMessage = "Photo";
                else
                    contact.lastMessage = "Empty";
            }
            contactDao.insert(contact);
            ContactUpgradeBus contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
            contactUpgradeBus.addAddressee(contact);
            MessageDao messageDao = database.messageDao();
            messageDao.insert(message);
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

    public void insertAddressee(Contact contact) {
        service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            contactDao.insert(contact);
            ContactUpgradeBus contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
            contactUpgradeBus.addAddressee(contact);
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
        try { return future.get(5, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }
}
