package com.yhaj.xr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 14:30
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Contact extends BaseBean {
    private String name;
    private String email;
    private String comment;
    private String subject;
    private Boolean alreadyRead = false;

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return jsonMapper.writeValueAsString(this).replace("\"","'");
    }
}
