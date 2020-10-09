package com.yhaj.xr.service;

import com.yhaj.xr.domain.User;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:35
 */
public interface UserService extends BaseService<User> {
    User login(User user);
}
