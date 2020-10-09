package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.Userdao;
import com.yhaj.xr.domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:27
 */
public class UserDaoImpl extends BaseDaoImpl<User> implements Userdao {
    @Override
    public boolean save(User bean) {
        return false;
    }

    @Override
    public User get(Integer id) {
        return null;
    }

    @Override
    public List<User> list() {
        return null;
    }

    public User login(User user) {
        String sql = "SELECT id, email, password FROM user WHERE email = ? and password = ? ";
        List<User> users = tpl.query(sql, new BeanPropertyRowMapper<>(User.class), user.getEmail(),
                user.getPassword());
        return users.isEmpty() ? null : users.get(0);
    }
}
