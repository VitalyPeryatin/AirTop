package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.example.infinity.airtop.ui.searchUser.SearchUserPresenter;

/**
 * Provides methods to {@link SearchUserPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class SearchUserInteractor extends BaseIntearctor {

    public void insertAddressee(Addressee addressee){
        service.submit(() -> {
            AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
            addresseeDao.insert(addressee);
        });
    }

}
