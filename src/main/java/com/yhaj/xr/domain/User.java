package com.yhaj.xr.domain;

import com.yhaj.xr.domain.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:12
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class User extends BaseBean {
    private String password;
    private String email;
    private Date birthday;
    private String photo;
    private String intro;
    private String name;
    private String address;
    private String phone;
    private String job;
    private String trait;
    private String interests;
}
