package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.SkillDao;
import com.yhaj.xr.domain.Skill;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 17:27
 */
public class SkillDaoImpl extends BaseDaoImpl<Skill> implements SkillDao {

    @Override
    public boolean save(Skill skill) {
        Integer id = skill.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(skill.getName());
        args.add(skill.getLevel());
        if (id == null || id <= 0) {
            sql = "INSERT INTO skill(name, level) VALUES (?, ?) ";
        } else {
            sql = "update skill SET name = ?, level = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Skill get(Integer id) {
        String sql = "SELECT id, name, level, created_time FROM skill WHERE id =? ";
        return tpl.queryForObject(sql,new BeanPropertyRowMapper<>(Skill.class),id);
    }

    @Override
    public List<Skill> list() {
        String sql = "SELECT id, name, level, created_time FROM skill  ";
        return tpl.query(sql,new BeanPropertyRowMapper<>(Skill.class));
    }
}
