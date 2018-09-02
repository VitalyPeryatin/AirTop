package com.infinity_coder.infinity.airtop.data.db.interactors;

import android.arch.lifecycle.LiveData;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.AppDatabase;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.ContactDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactUpgradeBus;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides methods to {@link ContactsFragment} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class ContactsInteractor extends BaseInteractor {

    private AppDatabase database = App.getInstance().getDatabase();


    public String getNicknameById(String id){
        Future<String> future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            return contactDao.getNicknameById(id);
        });
        try { return future.get(1, TimeUnit.SECONDS); }
        catch (InterruptedException | ExecutionException | TimeoutException e) { return null; }
    }

    public List<String> getUuidList(){
        Future<List<String>> future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            return contactDao.getUuidList();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<Contact> getLiveAddresseeByUuid(String uuid){
        Future<LiveData<Contact>> future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            return contactDao.getLiveAddresseeById(uuid);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Contact getContactByUuid(String uuid){
        Future<Contact> future = service.submit(() -> {
            ContactDao contactDao = database.addresseeDao();
            return contactDao.getContactById(uuid);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteAddressWithMessages(Contact contact){
        service.submit(() -> {
            MessageDao messageDao = database.messageDao();
            ContactDao contactDao = database.addresseeDao();
            ArrayList<Message> messages = (ArrayList<Message>) messageDao.getMessagesByUUID(contact.uuid);
            for (Message message : messages) {
                if(message.imageName != null){
                    File file = new File(App.getInstance().imagePath, message.imageName);
                    file.delete();
                }
            }
            messageDao.deleteByAddressId(contact.uuid);
            contactDao.delete(contact);
            ContactUpgradeBus contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
            contactUpgradeBus.removeAddressee(contact);
        });
    }

}
