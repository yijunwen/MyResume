package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.CompanyDao;
import com.yhaj.xr.domain.Company;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/1 16:44
 */
public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {

    @Override
    public boolean save(Company bean) {
        Integer id = bean.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getLogo());
        args.add(bean.getWebsite());
        args.add(bean.getIntro());
        if (id == null || id < 1) {
            sql = "INSERT INTO company(name, logo, website, intro ) values (? ,? ,? ,?)";
        } else {
            sql = "update company SET name = ?, logo = ?, website = ?, intro = ? WHERE id = ?";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Company get(Integer id) {
        String sql = "SELECT id,created_time,name,logo,website,intro FROM company WHERE id = ?";
        return tpl.queryForObject(sql,new BeanPropertyRowMapper<>(Company.class),id);
    }

    @Override
    public List<Company> list() {
        String sql = "SELECT id,created_time,name,logo,website,intro FROM company";
        return tpl.query(sql,new BeanPropertyRowMapper<>(Company.class));
    }
}
