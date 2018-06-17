package com.company.pojo.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response {
    private int code;
    private Object data;
    private String meg;
}
