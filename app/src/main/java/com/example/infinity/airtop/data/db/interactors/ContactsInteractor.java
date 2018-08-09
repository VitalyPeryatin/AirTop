package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides methods to {@link ContactsPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class ContactsInteractor extends BaseIntearctor{

    public ArrayList<Contact> getContacts(){
        Future<ArrayList<Contact>> future = service.submit(() -> {
            ArrayList<Contact> contacts = new ArrayList<>();
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Addressee> addressees = (ArrayList<Addressee>) addresseeDao.getAll();
            System.out.println();
            for (Addressee addressee : addressees) {
                Contact contact = new Contact();
                contact.addressee = addressee;
                Message message = messageDao.getLastMessageByAddressId(addressee.uuid);
                if(message == null){
                    deleteAddressWithMessages(addressee);
                }
                else {
                    contact.lastMessage = message.text;
                    contacts.add(contact);
                }
            }
            return contacts;
        });
        try {
            return future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
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
