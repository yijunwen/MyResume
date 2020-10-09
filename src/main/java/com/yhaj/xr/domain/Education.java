package com.yhaj.xr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/29 9:55
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class Education extends BaseBean {
    private String name;

    /**
     * 0: 其它
     * 1: 小学
     * 2: 初中
     * 3: 高中
     * 4: 中专
     * 5: 大专
     * 6: 本科
     * 7: 硕士
     * 8: 博士
     */
    private Integer type;
    private String intro;
    private Date beginDay;
    private Date endDay;

    @JsonIgnore
    public String getTypeString() {
        switch (type) {
            case 1: return "小学";
            case 2: return "初中";
            case 3: return "高中";
            case 4: return "中专";
            case 5: return "大专";
            case 6: return "本科";
            case 7: return "硕士";
            case 8: return "博士";
            default: return "其它";
        }
    }

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return jsonMapper.writeValueAsString(this).replace("\"","'");
    }
}
