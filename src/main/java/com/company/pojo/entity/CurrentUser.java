package com.company.pojo.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrentUser {
    private long id;
    private String username;
    private String role;
}
