package com.yhaj.xr.service;

import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 11:29
 */
public interface BaseService<T> {

    boolean remove(Integer id);

    boolean remove(List<Integer> ids);

    boolean save(T t);

    T get(Integer id);

    List<T> list();

    int count();
}
