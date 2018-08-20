package com.infinity_coder.infinity.airtop.data.db.interactors;

import android.arch.lifecycle.LiveData;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Addressee;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactsFragment;

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

    public HashMap<String, Contact> getUpdatedContacts(List<Addressee> addressees){
        Future<HashMap<String, Contact>> future = service.submit(() -> {
            contacts.clear();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            for (Addressee addressee : addressees) {
                Contact contact = new Contact();
                contact.addressee = addressee;
                Message message = messageDao.getLastMessageByAddressId(addressee.uuid);

                // If addressee.phone == null then unknown user sends the message
                // At this case first user is identified and after insert the message
                if(message == null){
                    deleteAddressWithMessages(addressee);
                }
                else {
                    contact.lastMessage = message.text;
                    contacts.put(addressee.uuid, contact);
                }
            }
            return contacts;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }

    public LiveData<List<Addressee>> getLiveAddress(){
        Future<LiveData<List<Addressee>>> future = service.submit(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Addressee> addressees = (ArrayList<Addressee>) addresseeDao.getAll();
            for (Addressee addressee : addressees) {
                Message message = messageDao.getLastMessageByAddressId(addressee.uuid);

                // If addressee.phone == null then unknown user sends the message
                // At this case first user is identified and after insert the message
                if(message == null && addressee.phone != null){
                    deleteAddressWithMessages(addressee);
                }
            }
            return addresseeDao.getAllLive();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAddressWithMessages(Addressee addressee){
        service.submit(() -> {
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            messageDao.deleteByAddressId(addressee.uuid);
            addresseeDao.delete(addressee);
        });
    }

}
