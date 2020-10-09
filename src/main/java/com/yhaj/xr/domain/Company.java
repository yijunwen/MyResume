package com.yhaj.xr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/1 16:21
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class Company extends BaseBean {
    private String name;
    private String logo;
    private String website;
    private String intro;

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        return new JsonMapper().writeValueAsString(this).replace("\"","'");
    }
}
