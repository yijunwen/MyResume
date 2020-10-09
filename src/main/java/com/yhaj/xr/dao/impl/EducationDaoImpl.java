package com.yhaj.xr.dao.impl;

import com.yhaj.xr.domain.Education;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/29 10:04
 */
public class EducationDaoImpl extends BaseDaoImpl<Education> {

    public boolean save(Education education) {
        Integer id = education.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(education.getName());
        args.add(education.getType());
        args.add(education.getIntro());
        args.add(education.getBeginDay());
        args.add(education.getEndDay());
        if (id == null || id <= 0) {
            sql = "INSERT INTO education(name, type, intro, begin_day, end_day) VALUES (?, ?, ?, ?, ?) ";
        } else {
            sql = "update education SET name = ?, type = ?, intro = ?, begin_day = ?, end_day = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    public Education get(Integer id) {
        String sql = "SELECT id,created_time,name, type, intro, begin_day, end_day FROM education WHERE id = ?";
        return tpl.queryForObject(sql, new BeanPropertyRowMapper<>(Education.class), id);
    }

    public List<Education> list() {
        String sql = "SELECT id,created_time,name, type, intro, begin_day, end_day FROM education";
        return tpl.query(sql, new BeanPropertyRowMapper<>(Education.class));
    }

}
