package com.yhaj.xr.service.impl;

import com.yhaj.xr.dao.BaseDao;
import com.yhaj.xr.service.BaseService;

import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 11:30
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseDao<T> dao = dao();

    protected BaseDao<T> dao() {
        try {
            String clsName = getClass().getName()
                    .replace(".service.", ".dao.")
                    .replace("Service", "Dao");
            return (BaseDao<T>) Class.forName(clsName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean remove(Integer id) {
        return dao.remove(id);
    }

    @Override
    public boolean remove(List<Integer> ids) {
        return dao.remove(ids);
    }

    @Override
    public boolean save(T t) {
        return dao.save(t);
    }

    @Override
    public T get(Integer id) {
        return dao.get(id);
    }

    @Override
    public List<T> list() {
        return dao.list();
    }

    @Override
    public int count() {
        return dao.count();
    }
}
