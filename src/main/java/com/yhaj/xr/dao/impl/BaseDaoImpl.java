package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.BaseDao;
import com.yhaj.xr.util.DBs;
import com.yhaj.xr.util.Strings;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/29 10:02
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    protected static JdbcTemplate tpl = DBs.getTpl();
    protected String tableName = setTableName();

    protected String setTableName() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class beanCls = (Class) type.getActualTypeArguments()[0];
        return Strings.underlineCase(beanCls.getSimpleName());
    }

    public boolean remove(Integer id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ? ";
        return tpl.update(sql, id) > 0;
    }

    public boolean remove(List<Integer> ids) {
        String sql = "DELETE FROM " + tableName + " WHERE id IN ( ";
        StringBuilder builder = new StringBuilder(sql);
        List<Object> args = new ArrayList<>();
        for (Integer id : ids) {
            builder.append("?, ");
            args.add(id);
        }
        builder.replace(builder.length() - 2, builder.length(), " )");
        return tpl.update(builder.toString(), args.toArray()) > 0;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        return tpl.queryForObject(sql, new BeanPropertyRowMapper<>(Integer.class));
    }

}
