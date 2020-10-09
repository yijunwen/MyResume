package com.yhaj.xr.dao;

import com.yhaj.xr.domain.User;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:27
 */
public interface Userdao extends BaseDao<User> {
    User login(User user);
}
