package com.company.pojo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user")
public class User extends BaseModel {
    private String username;
    private String password;
    private String role;
}
