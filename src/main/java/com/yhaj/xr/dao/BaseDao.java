package com.yhaj.xr.dao;

import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 16:53
 */
public interface BaseDao<T> {
    boolean remove(Integer id);
    boolean remove(List<Integer> ids);
    boolean save(T bean);
    T get(Integer id);
    List<T> list();
    int count();
}
