package com.yhaj.xr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/2 13:48
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Award extends BaseBean {
    private String name;
    private String image;
    private String intro;

    @JsonIgnore
    public String getJson() throws Exception {
        return new JsonMapper().writeValueAsString(this).replace("\"", "'");
    }
}
