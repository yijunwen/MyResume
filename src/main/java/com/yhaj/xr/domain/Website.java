package com.yhaj.xr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/28 15:22
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class Website extends BaseBean {
    private String footer;
}
