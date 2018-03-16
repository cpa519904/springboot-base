package com.company.pojo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
//@Entity
//@Table(name = "app_config")
public class AppConfig extends BaseModel implements Serializable {
    //@Id
    //@GeneratedValue
    private Long id;
    private String appId;
    private String type;
    private String thirdAppId;
    private String thirdMerchantNo;
    private String secretKey;
    private String notifyUrl;
}
