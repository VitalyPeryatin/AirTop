package com.infinity_coder.infinity.airtop.data.db.interactors;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Addressee;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.infinity_coder.infinity.airtop.ui.searchUser.SearchUserPresenter;

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
