package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.ContactDao;
import com.yhaj.xr.domain.Contact;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:23
 */
public class ContactDaoImpl extends BaseDaoImpl<Contact> implements ContactDao {
    @Override
    public boolean save(Contact bean) {
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getEmail());
        args.add(bean.getComment());
        args.add(bean.getSubject());
        args.add(bean.getAlreadyRead());
        String sql = "INSERT INTO contact (name, email, comment, subject, already_read) VALUES (?, ?, ?, ?, ?) ";
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Contact get(Integer id) {
        String sql = "SELECT id, created_time, name, email, comment, subject, already_read FROM contact WHERE id = ? ";
        return tpl.queryForObject(sql, new BeanPropertyRowMapper<>(Contact.class), id);
    }

    @Override
    public List<Contact> list() {
        String sql = "SELECT id, created_time, name, email, comment, subject, already_read FROM contact ";
        return tpl.query(sql, new BeanPropertyRowMapper<>(Contact.class));
    }
}
