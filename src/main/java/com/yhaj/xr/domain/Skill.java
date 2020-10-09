package com.yhaj.xr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 17:23
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Skill extends BaseBean {
    private String name;
    /**
     * 0：了解
     * 1：熟悉
     * 2：掌握
     * 3：精通
     */
    private Integer level;

    @JsonIgnore
    public String getLevelString() {
        switch (level) {
            case 1:
                return "熟悉";
            case 2:
                return "掌握";
            case 3:
                return "精通";
            default:
                return "了解";
        }
    }

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        return new JsonMapper().writeValueAsString(this).replace("\"","'");
    }
}
