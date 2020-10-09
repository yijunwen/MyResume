package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.AwardDao;
import com.yhaj.xr.domain.Award;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/2 13:51
 */
public class AwardDaoImpl extends BaseDaoImpl<Award> implements AwardDao {

    @Override
    public boolean save(Award bean) {
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getImage());
        args.add(bean.getIntro());
        Integer id = bean.getId();
        if (id == null || id < 1) {
            sql = "INSERT INTO award(name, image, intro) VALUES (?, ?, ?) ";
        } else {
            sql = "UPDATE award SET name = ?, image = ?, intro = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Award get(Integer id) {
        String sql = "SELECT id, created_time, name, image, intro FROM award  WHERE id = ? ";
        return tpl.queryForObject(sql, new BeanPropertyRowMapper<>(Award.class), id);
    }

    @Override
    public List<Award> list() {
        String sql = "SELECT id, created_time, name, image, intro FROM award ";
        return tpl.query(sql, new BeanPropertyRowMapper<>(Award.class));
    }
}
