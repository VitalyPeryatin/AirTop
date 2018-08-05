package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;

public class SearchUserInteractor extends BaseIntearctor {

    public void insertAddressee(Addressee addressee){
        service.submit(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            addresseeDao.insert(addressee);
        });
    }

}
