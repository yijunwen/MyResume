package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.ExperienceDao;
import com.yhaj.xr.domain.Company;
import com.yhaj.xr.domain.Experience;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/2 10:02
 */
public class ExperienceDaoImpl extends BaseDaoImpl<Experience> implements ExperienceDao {

    private static String querySql;
    private static RowMapper<Experience> rowMapper;

    static {
        querySql = "SELECT e.id, e.created_time, e.job, e.intro ,e.begin_day, e.end_day, c.id,c.created_time , c" +
                ".name, c.intro, c.website, c.logo FROM experience e left join company c ON e.company_id = c.id ";
        rowMapper = (ResultSet resultSet, int i) -> {
            Experience experience = new Experience();
            experience.setId(resultSet.getInt("e.id"));
            experience.setCreatedTime(resultSet.getDate("e.created_time"));
            experience.setIntro(resultSet.getString("e.intro"));
            experience.setJob(resultSet.getString("e.job"));
            experience.setBeginDay(resultSet.getDate("e.begin_day"));
            experience.setEndDay(resultSet.getDate("e.end_day"));

            Company company = new Company();
            experience.setCompany(company);

            company.setId(resultSet.getInt("c.id"));
            company.setCreatedTime(resultSet.getDate("c.created_time"));
            company.setIntro(resultSet.getString("c.intro"));
            company.setLogo(resultSet.getString("c.logo"));
            company.setWebsite(resultSet.getString("c.website"));
            company.setName(resultSet.getString("c.name"));
            return experience;
        };
    }

    @Override
    public boolean save(Experience bean) {
        Integer id = bean.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(bean.getJob());
        args.add(bean.getIntro());
        args.add(bean.getBeginDay());
        args.add(bean.getEndDay());
        args.add(bean.getCompany().getId());
        if (null == id || id < 1) {
            sql = "INSERT INTO experience(job, intro, begin_day, end_day, company_id) VALUES (?, ?, ?, ?, ?) ";
        } else {
            sql = "UPDATE experience SET job = ?, intro = ?, begin_day = ?, end_day = ?, company_id = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Experience get(Integer id) {
        return tpl.queryForObject(querySql + " WHERE e.id = ? ", rowMapper, id);
    }

    @Override
    public List<Experience> list() {
        return tpl.query(querySql, rowMapper);
    }
}
