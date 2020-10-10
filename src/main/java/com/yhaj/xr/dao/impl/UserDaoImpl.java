package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.Userdao;
import com.yhaj.xr.domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:27
 */
public class UserDaoImpl extends BaseDaoImpl<User> implements Userdao {
    @Override
    public boolean save(User bean) {
        String sql = "UPDATE user SET email = ?, password = ?, birthday = ?, photo = ?, intro = ?, name = ?, address = ?, phone = " +
                "?, job = ?,trait = ?, interests = ? WHERE id = ? ";
        List<Object> args = new ArrayList<>();
        args.add(bean.getEmail());
        args.add(bean.getPassword());
        args.add(bean.getBirthday());
        args.add(bean.getPhoto());
        args.add(bean.getIntro());
        args.add(bean.getName());
        args.add(bean.getAddress());
        args.add(bean.getPhone());
        args.add(bean.getJob());
        args.add(bean.getTrait());
        args.add(bean.getInterests());
        args.add(bean.getId());
        return tpl.update(sql, args.toArray()) > 0;
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
        String sql = "SELECT id, email, password, birthday, photo, intro, name, address, phone, job, trait, interests" +
                " FROM user WHERE email = ? and password = ? ";
        List<User> users = tpl.query(sql, new BeanPropertyRowMapper<>(User.class), user.getEmail(),
                user.getPassword());
        return users.isEmpty() ? null : users.get(0);
    }
}
