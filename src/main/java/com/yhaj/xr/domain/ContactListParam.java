package com.yhaj.xr.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/13 10:54
 */
@Data
public class ContactListParam {
    public static final int READ_ALL = 2;
    private Integer pageNo;
    private Integer pageSize;
    private Date beginDay;
    private Date endDay;
    private String keyword;
    /**
     * 0：未读
     * 1：已读
     * 2：全部
     */
    private Integer alreadyRead;
}
