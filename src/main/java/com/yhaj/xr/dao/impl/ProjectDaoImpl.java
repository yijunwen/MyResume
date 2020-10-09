package com.yhaj.xr.dao.impl;

import com.yhaj.xr.dao.ProjectDao;
import com.yhaj.xr.domain.Company;
import com.yhaj.xr.domain.Project;
import org.springframework.jdbc.core.RowMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/7 17:53
 */
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao {
    private static String querySql;
    private static RowMapper<Project> rowMapper;

    static {
        querySql = "SELECT p.id, p.created_time, p.name, p.intro, p.website, p.image, p.begin_day, p.end_day, c.id,c" +
                ".created_time , c.name, c.intro, c.website, c.logo FROM project p left join company c ON p" +
                ".company_id = c.id ";

        rowMapper = (resultSet, i) -> {
            Project project = new Project();
            project.setId(resultSet.getInt("p.id"));
            project.setCreatedTime(resultSet.getDate("p.created_time"));
            project.setName(resultSet.getString("p.name"));
            project.setIntro(resultSet.getString("p.intro"));
            project.setWebsite(resultSet.getString("p.website"));
            project.setImage(resultSet.getString("p.image"));
            project.setBeginDay(resultSet.getDate("p.begin_day"));
            project.setEndDay(resultSet.getDate("p.end_day"));

            Company company = new Company();
            project.setCompany(company);
            company.setId(resultSet.getInt("c.id"));
            company.setCreatedTime(resultSet.getDate("c.created_time"));
            company.setIntro(resultSet.getString("c.intro"));
            company.setLogo(resultSet.getString("c.logo"));
            company.setWebsite(resultSet.getString("c.website"));
            company.setName(resultSet.getString("c.name"));
            return project;
        };
    }

    @Override
    public boolean save(Project bean) {
        Integer id = bean.getId();
        String sql;
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getIntro());
        args.add(bean.getWebsite());
        args.add(bean.getImage());
        args.add(bean.getBeginDay());
        args.add(bean.getEndDay());
        args.add(bean.getCompany().getId());
        if (null == id || id < 1) {
            sql = "INSERT INTO project(name, intro, website, image, begin_day, end_day, company_id) VALUES (?, ?, ?, " +
                    "?, ?, ?, ?) ";
        } else {
            sql = "UPDATE project SET name = ?, intro = ?, website = ?, image = ?, begin_day = ?, end_day = ?, " +
                    "company_id = ? WHERE id = ? ";
            args.add(id);
        }
        return tpl.update(sql, args.toArray()) > 0;
    }

    @Override
    public Project get(Integer id) {
        return tpl.queryForObject(querySql + " WHERE t1.id = ? ", rowMapper, id);
    }

    @Override
    public List<Project> list() {
        return tpl.query(querySql, rowMapper);
    }
}
