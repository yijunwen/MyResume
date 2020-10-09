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
 * @date 2020/10/1 21:19
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Experience extends BaseBean {
    private String job;
    private String intro;
    private Date beginDay;
    private Date endDay;
    private Company company;

    @JsonIgnore
    public Company getCompany() {
        return company;
    }

    public Integer getCompanyId(){
        return company.getId();
    }

    @JsonIgnore
    public String getJson() throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return jsonMapper.writeValueAsString(this).replace("\"","'");
    }
}
