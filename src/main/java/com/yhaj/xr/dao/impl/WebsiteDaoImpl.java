package com.yhaj.xr.dao.impl;

import com.yhaj.xr.domain.Website;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/28 15:32
 */
public class WebsiteDaoImpl extends BaseDaoImpl<Website> {

    public boolean save(Website website) {
        Integer id = website.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(website.getFooter());
        if (id == null || id <= 0) {
            sql = "INSERT INTO website(footer) VALUES (?) ";
        } else {
            sql = "update website SET footer = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Website get(Integer id) {
        return null;
    }

    public Website get() {
        return null;
    }

    public List<Website> list() {
        String sql = "SELECT id,created_time,footer FROM website";
        return tpl.query(sql, new BeanPropertyRowMapper<>(Website.class));
    }

}
