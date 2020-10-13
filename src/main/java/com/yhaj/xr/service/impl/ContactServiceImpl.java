package com.yhaj.xr.service.impl;

import com.yhaj.xr.dao.ContactDao;
import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.domain.ContactListParam;
import com.yhaj.xr.domain.ContactListResult;
import com.yhaj.xr.service.ContactService;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:35
 */
public class ContactServiceImpl extends BaseServiceImpl<Contact> implements ContactService {
    @Override
    public ContactListResult list(ContactListParam param) {
        return ((ContactDao) dao).list(param);
    }

    @Override
    public boolean read(Integer id) {
        return ((ContactDao) dao).read(id);
    }
}
