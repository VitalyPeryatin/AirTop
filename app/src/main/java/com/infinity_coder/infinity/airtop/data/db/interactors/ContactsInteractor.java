package com.infinity_coder.infinity.airtop.data.db.interactors;

import android.arch.lifecycle.LiveData;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.ContactDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactUpgradeBus;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Provides methods to {@link ContactsFragment} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class ContactsInteractor extends BaseIntearctor{

    private HashMap<String, Contact> contacts = new HashMap<>();
    private ContactUpgradeBus contactUpgradeBus;

    public ContactsInteractor(){
        contactUpgradeBus = App.getInstance().getResponseListeners().getContactUpgradeBus();
    }

    public HashMap<String, Contact> getUpdatedContacts(){
        Future<HashMap<String, Contact>> future = service.submit(() -> {
            this.contacts.clear();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ContactDao contactDao = App.getInstance().getDatabase().addresseeDao();
            ArrayList<Contact> contacts = (ArrayList<Contact>) contactDao.getAll();
            for (Contact addressee : contacts) {
                Message message = messageDao.getLastMessageByAddressId(addressee.uuid);

                // If addressee.phone == null then unknown user sends the message
                // At this case first user is identified and after insert the message
                if(message == null){
                    deleteAddressWithMessages(addressee);
                }
                else {
                    this.contacts.put(addressee.uuid, addressee);
                }
            }
            return this.contacts;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getUuidList(){
        Future<List<String>> future = service.submit(() -> {
            ContactDao contactDao = App.getInstance().getDatabase().addresseeDao();
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
            ContactDao contactDao = App.getInstance().getDatabase().addresseeDao();
            return contactDao.getLiveAddresseeById(uuid);
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
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ContactDao contactDao = App.getInstance().getDatabase().addresseeDao();
            ArrayList<Message> messages = (ArrayList<Message>) messageDao.getMessagesByUUID(contact.uuid);
            for (Message message : messages) {
                if(message.imagePath != null){
                    File file = new File(message.imagePath);
                    file.delete();
                }
            }
            messageDao.deleteByAddressId(contact.uuid);
            contactDao.delete(contact);
            contactUpgradeBus.removeAddressee(contact);
        });
    }

}
