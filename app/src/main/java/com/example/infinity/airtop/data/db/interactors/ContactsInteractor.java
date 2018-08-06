package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Contact;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContactsInteractor extends BaseIntearctor{

    public ArrayList<Contact> getContacts(){
        Future<ArrayList<Contact>> future = service.submit(() -> {
            ArrayList<Contact> contacts = new ArrayList<>();
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            MessageDao messageDao = App.getInstance().getDatabase().messageDao();
            ArrayList<Addressee> addressees = (ArrayList<Addressee>) addresseeDao.getAll();
            for (Addressee addressee : addressees) {
                Contact contact = new Contact();
                contact.setAddressee(addressee);
                Message message = messageDao.getLastMessageByAddressePhone(addressee.phone);
                if(message == null){
                    addresseeDao.delete(addressee);
                }
                else {
                    contact.setLastMessage(message.text);
                    contacts.add(contact);
                }
            }
            return contacts;
        });
        try {
            return future.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

}