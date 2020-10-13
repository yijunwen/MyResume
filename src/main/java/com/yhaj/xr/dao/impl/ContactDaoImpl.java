package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.ContactDao;
import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.domain.ContactListParam;
import com.yhaj.xr.domain.ContactListResult;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:23
 */
public class ContactDaoImpl extends BaseDaoImpl<Contact> implements ContactDao {

    private final static String SELECT = "SELECT id, created_time, name, email, comment, subject, already_read FROM " +
            "contact ";

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
        String sql = SELECT + "WHERE id = ? ";
        return tpl.queryForObject(sql, new BeanPropertyRowMapper<>(Contact.class), id);
    }

    @Override
    public List<Contact> list() {
        return tpl.query(SELECT, new BeanPropertyRowMapper<>(Contact.class));
    }

    public ContactListResult list(ContactListParam param) {
        ContactListResult result = new ContactListResult();
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder condition = new StringBuilder();
        sql.append(SELECT);
        condition.append("WHERE 1 = 1 ");
        Integer read = param.getAlreadyRead();
        if (read != null && read != ContactListParam.READ_ALL) {
            condition.append("AND already_read = ? ");
            args.add(read);
            result.setAlreadyRead(read);
        }
        Date beginDay = param.getBeginDay();
        if (beginDay != null) {
            condition.append("AND created_time >= ? ");
            args.add(param.getBeginDay());
            result.setBeginDay(beginDay);
        }
        Date endDay = param.getEndDay();
        if (endDay != null) {
            condition.append("AND created_time <= ? ");
            args.add(param.getEndDay());
            result.setEndDay(endDay);
        }
        String keyword = param.getKeyword();
        if (keyword != null && !keyword.isEmpty()) {
            condition.append("AND (name LIKE ? OR email LIKE ? OR subject LIKE ? OR comment LIKE ?) ");
            result.setKeyword(keyword);
            keyword = "%" + keyword + "%";
            args.add(keyword);
            args.add(keyword);
            args.add(keyword);
            args.add(keyword);
        }

        Integer pageSize = param.getPageSize();
        if (pageSize == null || pageSize < 10) {
            pageSize = 10;
        }
        String countSql = "SELECT COUNT(*) FROM contact " + condition.toString();
        Integer totalCount = tpl.queryForObject(countSql, Integer.class, args.toArray());
        int totalPages = (totalCount + pageSize - 1) / pageSize;
        if (totalCount == 0) return result;
        result.setTotalCount(totalCount);
        result.setTotalPages(totalPages);

        Integer pageNo = param.getPageNo();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        } else if (pageNo > totalPages) {
            pageNo = totalPages;
        }
        sql.append(condition).append("LIMIT ?, ? ");
        args.add((pageNo - 1) * pageSize);
        args.add(pageSize);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);

        List<Contact> contacts = tpl.query(sql.toString(), new BeanPropertyRowMapper<>(Contact.class), args.toArray());
        result.setContacts(contacts);
        return result;
    }

    public boolean read(Integer id) {
        String sql = "UPDATE contact SET already_read = 1 WHERE id = ? ";
        return tpl.update(sql, id) > 0;
    }
}
