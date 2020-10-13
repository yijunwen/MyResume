package com.yhaj.xr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/13 10:54
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ContactListResult extends ContactListParam{
    /**
     * 总数量
     */
    private Integer totalCount;
    /**
     * 总页数
     */
    private Integer totalPages;
    private List<Contact> contacts;
}
