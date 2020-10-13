package com.yhaj.xr.dao;

import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.domain.ContactListParam;
import com.yhaj.xr.domain.ContactListResult;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:22
 */
public interface ContactDao extends BaseDao<Contact> {
    ContactListResult list(ContactListParam param);
    boolean read(Integer id);
}
