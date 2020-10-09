package com.yhaj.xr.service.impl;

import com.yhaj.xr.dao.impl.UserDaoImpl;
import com.yhaj.xr.domain.User;
import com.yhaj.xr.service.UserService;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:36
 */
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Override
    public User login(User user) {
        return ((UserDaoImpl)dao).login(user);
    }
}
