package com.infinity_coder.infinity.airtop.data.db.interactors;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.ContactDao;
import com.infinity_coder.infinity.airtop.ui.searchUser.SearchUserPresenter;

/**
 * Provides methods to {@link SearchUserPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class SearchUserInteractor extends BaseIntearctor {

    public void insertAddressee(Contact contact){
        service.submit(() -> {
            ContactDao contactDao = App.getInstance().getDatabase().addresseeDao();
            contactDao.insert(contact);
        });
    }

}
