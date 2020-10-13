package com.yhaj.xr.service;

import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.domain.ContactListParam;
import com.yhaj.xr.domain.ContactListResult;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:34
 */
public interface ContactService extends BaseService<Contact> {
    ContactListResult list(ContactListParam param);
    boolean read(Integer id);
}
