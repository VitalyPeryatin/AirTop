package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.ui.contacts.ContactsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides methods to {@link ContactsFragment} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class ContactsInteractor extends BaseIntearctor{

    public HashMap<String, Contact> getContacts(){
        Future<HashMap<String, Contact>> future = service.submit(() -> {
            HashMap<String, Contact> contacts = new HashMap<>();
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Addressee> addressees = (ArrayList<Addressee>) addresseeDao.getAll();
            System.out.println(addressees.size()+"");
            for (Addressee addressee : addressees) {
                Contact contact = new Contact();
                contact.addressee = addressee;
                Message message = messageDao.getLastMessageByAddressId(addressee.uuid);

                // If addressee.phone == null then unknown user sends the message
                // At this case first user is identified and after insert the message
                if(message == null && addressee.phone != null){
                    deleteAddressWithMessages(addressee);
                }
                else {
                    contact.lastMessage = message.text;
                    contacts.put(addressee.uuid, contact);
                }
            }
            System.out.println();
            return contacts;
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
