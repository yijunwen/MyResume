package com.yhaj.xr.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.yhaj.xr.dao.impl.WebsiteDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 11:31
 */
public class DBs {
    private static JdbcTemplate tpl;

    static {
        try (InputStream is = WebsiteDaoImpl.class.getClassLoader().getResourceAsStream("druid.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            tpl = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JdbcTemplate getTpl(){
        return tpl;
    }
}
